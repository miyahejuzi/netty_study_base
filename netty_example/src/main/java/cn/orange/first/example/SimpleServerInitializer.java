package cn.orange.first.example;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author kz
 * @date 2019/8/27
 */
public class SimpleServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 一旦 channel 被注册后, 这个方法就会被调用 (回调)
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 管道
        ChannelPipeline pipeline = ch.pipeline();

        pipeline
                // 对 web 的请求进行编解码
                .addLast("http server codec", new HttpServerCodec())
                .addLast("simple http server handler", new SimpleHttpServerHandler());

    }
}
