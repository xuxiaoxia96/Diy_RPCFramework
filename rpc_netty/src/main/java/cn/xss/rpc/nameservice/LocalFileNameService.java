package cn.xss.rpc.nameservice;

import cn.xss.rpc.NameService;
import cn.xss.rpc.serialize.SerializeSupport;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

//本地文件是共享资源，一定要加锁，它会被 RPC 框架所有的客户端和服务端并发读写
//这个文件可能被多个进程读写，所以这里不能使用我们之前讲过的，编程语言提供的那些锁，
// 原因是这些锁只能在进程内起作用，它锁不住其他进程
//应该用进程锁，也就是操作系统提供的————文件锁

public class LocalFileNameService implements NameService {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(LocalFileNameService.class);
    private static final Collection<String> schemes = Collections.singleton("file");
    private File file;


    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {
        if(schemes.contains(nameServiceUri.getScheme())){
            file = new File(nameServiceUri);
        }else {
            throw new RuntimeException("Unsuported scheme");
        }
    }

    //注册服务，把服务提供者保存到本地文件
    @Override
    public synchronized void registerService(String serviceName, URI uri) throws IOException {
        logger.info("Register service: {}, uri: {}.", serviceName, uri);
        try(RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                int fileLength = (int) raf.length();
                Metadata metadata;
                byte[] bytes;
                if(fileLength > 0) {
                    bytes = new byte[(int) raf.length()];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }

                    metadata = SerializeSupport.parse(bytes);
                } else {
                    metadata = new Metadata();
                }
                List<URI> uris = metadata.computeIfAbsent(serviceName, k -> new ArrayList<>());
                if(!uris.contains(uri)) {
                    uris.add(uri);
                }
                logger.info(metadata.toString());

                bytes = SerializeSupport.serialize(metadata);
                fileChannel.truncate(bytes.length);
                fileChannel.position(0L);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);
            } finally {
                lock.release();
            }
        }
    }

    //查找服务，去本地文件中读出所有的服务提供者，找到对应的服务提供者，然后返回。
    @Override
    public URI lookupService(String serviceName) throws IOException {
        Metadata metadata;
        try(RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                byte [] bytes = new byte[(int) raf.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }
                metadata = bytes.length == 0? new Metadata(): (Metadata) SerializeSupport.parse(bytes);
                logger.info(metadata.toString());
            } finally {
                lock.release();
            }
        }

        List<URI> uris = metadata.get(serviceName);
        if(null == uris || uris.isEmpty()) {
            return null;
        } else {
            return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
        }
    }
}
