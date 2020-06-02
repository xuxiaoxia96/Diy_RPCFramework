package cn.xss.rpc.client.stubs;

import cn.xss.rpc.client.ServiceStub;
import cn.xss.rpc.client.ServiceTypes;
import cn.xss.rpc.client.RequestIdSupport;
import cn.xss.rpc.serialize.SerializeSupport;
import cn.xss.rpc.transport.Transport;
import cn.xss.rpc.transport.command.Code;
import cn.xss.rpc.transport.command.Command;
import cn.xss.rpc.transport.command.Header;
import cn.xss.rpc.transport.command.ResponseHeader;

import java.util.concurrent.ExecutionException;

public abstract class AbstractStub implements ServiceStub {
    protected Transport transport;

    protected byte [] invokeRemote(RpcRequest request) {
        Header header = new Header(ServiceTypes.TYPE_RPC_REQUEST, 1, RequestIdSupport.next());
        byte [] payload = SerializeSupport.serialize(request);
        Command requestCommand = new Command((com.sun.xml.internal.ws.api.message.Header) header, payload);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if(responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}