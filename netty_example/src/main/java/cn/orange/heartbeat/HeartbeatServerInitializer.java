package cn.orange.heartbeat;

import cn.orange.scoket.cliser.ClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author kz
 * @date 2019/8/29
 */
public class HeartbeatServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 这种一个个的 handler 的处理完, 然后一个个返回
        // 就像 filterChain 之类的东西
        // 这叫 责任链模式
        pipeline
                // 一个空闲状态监测的处理器, 单位秒
                .addLast(new IdleStateHandler(5, 7, 10, TimeUnit.SECONDS))
                .addLast(new HeartbeatServerHandler());

    }
}









