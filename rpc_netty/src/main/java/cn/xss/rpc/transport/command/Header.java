package cn.xss.rpc.transport.command;
/*
requestId 用于唯一标识一个请求命令，在我们使用双工方式异步收发数据的时候，
这个 requestId 可以用于请求和响应的配对
version 这个属性用于标识这条命令的版本号。type 用于标识这条命令的类型，
这个类型主要的目的是为了能让接收命令一方来识别收到的是什么命令，以便路由到对应的处理类中去。
 */
public class Header {
    private int requestId;
    private int version;
    private int type;

    public Header() {}
    public Header(int type, int version, int requestId) {
        this.requestId = requestId;
        this.type = type;
        this.version = version;
    }
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES;
    }

    public void setType(int type) {
        this.type = type;
    }
}