package cn.orange.chat.cliser;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author kz
 * @date 2019/8/28
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 只需要打印服务端返回给客户端的数据就行了
     * 发送消息到服务端的代码在 ChatClient 里编写
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
