package cn.orange.scoket.cliser;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author kz
 * @date 2019/8/28
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 连接建立之后, 这个方法就就会自动调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("通道建立后, 向服务端发送数据.");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " --> " + msg);
        channel.writeAndFlush("form client : ' world...'");
        Thread.sleep(1000);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
