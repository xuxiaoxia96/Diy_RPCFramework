package cn.xss.rpc.transport;

import cn.xss.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

public interface Transport {
    /**
     * 发送请求命令
     * @param request 请求命令
     * @return 返回值是一个 Future，Future
     */
    CompletableFuture<Command> send(Command request);
}