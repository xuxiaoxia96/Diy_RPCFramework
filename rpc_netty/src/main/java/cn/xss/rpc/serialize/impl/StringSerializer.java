package cn.xss.rpc.serialize.impl;

import cn.xss.rpc.serialize.Serializer;

import java.nio.charset.StandardCharsets;

public class StringSerializer implements Serializer<String> {

    public int size(String entry) {
        return entry.getBytes(StandardCharsets.UTF_8).length;
    }

    public void serialize(String entry, byte[] bytes, int offset, int length) {
        byte[] strBytes = entry.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(strBytes, 0, bytes, offset, strBytes.length);
    }

    public String parse(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length, StandardCharsets.UTF_8);
    }

    public byte type() {
        return Types.TYPE_STRING;
    }

    public Class<String> getSerializeClass() {
        return String.class;
    }
}
