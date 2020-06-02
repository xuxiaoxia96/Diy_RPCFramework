package cn.xss.rpc.client;

import cn.xss.rpc.transport.Transport;


public interface StubFactory {
    <T> T createStub(Transport transport, Class<T> serviceClass);
}
