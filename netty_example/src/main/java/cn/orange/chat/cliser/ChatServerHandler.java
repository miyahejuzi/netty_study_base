package cn.orange.chat.cliser;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Objects;

/**
 * @author kz
 * @date 2019/8/28
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当通道(链接)建立的时候, 此方法就会被调用
     * 实现业务 : 向其他所有通道发送此链接链接成功的的通知(用户上线的通知)
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 链接\n");
        channelGroup.add(channel);
    }

    /**
     * 当通道(链接)断掉的时候, 此方法就会被调用
     * 实现业务 : 向其他所有通道发送此链接断开链接的的通知(用户下线的通知)
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 断开链接\n");
        // 当链接断开的时候, netty 会自动的搜索全局, 将链接从全局(Global)变量里面删除
        // 所以这行代码不需要书写
        // channelGroup.remove(channel);
    }

    /**
     * 此链接激活时, 此方法会被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " -> 上线");
    }

    /**
     * 此链接变为非活动状态时, 此方法会被调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " -> 下线");
    }

    /**
     * 当收到消息的时, 此方法会被调用
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (!Objects.equals(channel, ch)) {
                ch.writeAndFlush(ch.remoteAddress() + " 发送 : " + msg + "\n");
            } else {
                ch.writeAndFlush("【自己】 发送 : " + msg + "\n");
            }
        });
    }

    /**
     * 当出现了异常时, 此方法会被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
