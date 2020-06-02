package cn.xss.rpc.transport.command;

import com.sun.xml.internal.ws.api.message.Header;
/*
Command 类包含一个命令头 Header 和一个 payload 字节数组。
payload 就是命令中要传输的数据，这里我们要求这个数据已经是被序列化之后生成的字节数组
 Header中包含三个属性
 */
public class Command {
    protected Header header;
    private byte[] payload;

    public Command(Header header, byte[] payload){
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
