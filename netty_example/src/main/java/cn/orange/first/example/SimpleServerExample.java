package cn.orange.first.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 简单的 http 服务器的例子
 * 基本流程都是这样样子
 *
 * @author kz
 * @date 2019/8/27
 */
public class SimpleServerExample {

    public static void main(String[] args) throws Exception {
        // boss 不断从客户端接收链接, 但不处理, 转发给 worker 处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 便捷的启动服务器的一个类, 对启动服务器做了一些基本的封装
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 定义初始化处理器, 在 channel 一旦被注册好之后, 它就会被创建, 并执行里面的方法
                    .childHandler(new SimpleServerInitializer());
            // 绑定端口
            ChannelFuture future = serverBootstrap.bind(8899).sync();
            future.channel().closeFuture().sync();
        } finally {
            // 优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}


















