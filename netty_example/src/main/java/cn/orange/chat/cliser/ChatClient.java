package cn.orange.chat.cliser;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 用于连接我自己写的 server 的客户端
 *
 * @author kz
 * @date 2019/8/28
 */
public class ChatClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try (
                // 标准输入流, 也就是控制台的键盘输入
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
        ) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer());

            // 不知道这里为什么这么写, 为什么区别于其他的例子
            Channel channel = bootstrap.connect("localhost", 8899).sync().channel();

            // 死循环的和服务器端进行通讯
            // bio 只有当我按下回车的时候, 才会读取完一行, 并且向 channel 里写一行数据
            while (true) {
                channel.writeAndFlush(br.readLine() + "\r\n");
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
