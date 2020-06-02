package cn.xss.rpc.transport.netty;

import cn.xss.rpc.transport.command.Command;
import cn.xss.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public abstract class CommandEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(!(o instanceof Command)) {
            throw new Exception(String.format("Unknown type: %s!", o.getClass().getCanonicalName()));
        }

        Command command = (Command) o;
        byteBuf.writeInt(Integer.BYTES + command.getHeader().getStringContent().length() + command.getPayload().length);
        encodeHeader(channelHandlerContext, (Header) command.getHeader(), byteBuf);
        byteBuf.writeBytes(command.getPayload());


    }

    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(header.getType());
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getRequestId());
    }
}
