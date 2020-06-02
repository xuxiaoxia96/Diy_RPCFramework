package cn.xss.rpc.serialize.impl;

import cn.xss.rpc.client.stubs.RpcRequest;
import cn.xss.rpc.serialize.Serializer;

public class RpcRequestSerializer implements Serializer<RpcRequest> {
    @Override
    public int size(RpcRequest entry) {
        return 0;
    }

    @Override
    public void serialize(RpcRequest entry, byte[] bytes, int offset, int length) {

    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        return null;
    }

    @Override
    public byte type() {
        return 0;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return null;
    }
}
