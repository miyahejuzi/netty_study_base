package cn.orange.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.time.LocalDateTime;

/**
 * web socket 实现了真的长链接
 *
 * @author kz
 * @date 2019/8/29
 */
public class WebSocketServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 写了这么多次, 也就知道了也就三个大的组件
                    .childHandler(new WebSocketServerInitializer());

            ChannelFuture future = bootstrap.bind(8899).sync();
            future.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private static class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline()
                    .addLast(new HttpServerCodec())
                    // 以块的方式写的一个处理器
                    .addLast(new ChunkedWriteHandler())
                    // 对一个 http 消息进行聚合, 可能一个http数据太多,
                    // 被分段发送了, 那么就会将分段的组合起来当做一个请求
                    .addLast(new HttpObjectAggregator(8192))
                    // 控制 webSocket 的握手
                    // 心跳, ping pong
                    // /ws 是开启 websocket 的请求 path
                    .addLast(new WebSocketServerProtocolHandler("/ws"))
                    .addLast(new TestWebSocketServerHandler());
        }
    }

    private static class TestWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            System.out.println("收到了消息 : " + msg.text());
            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器  " + LocalDateTime.now() + "  返回了一个消息..."));
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            System.out.println("handlerAdded: " + ctx.channel().id().asLongText());
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            System.out.println("handlerRemoved: " + ctx.channel().id());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println(cause.getMessage());
            ctx.channel().close();
        }
    }
}
