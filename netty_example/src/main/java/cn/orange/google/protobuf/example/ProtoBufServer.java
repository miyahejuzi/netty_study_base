package cn.orange.google.protobuf.example;


import cn.orange.google.protobuf.proto.DataInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * web socket 实现了真的长链接
 *
 * @author kz
 * @date 2019/8/29
 */
public class ProtoBufServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ProtoBufServerInitializer());

            ChannelFuture future = bootstrap.bind(8899).sync();
            future.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private static class ProtoBufServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                    .addLast((new ProtobufVarint32LengthFieldPrepender()))
                    .addLast(new ProtobufDecoder(DataInfo.Data.getDefaultInstance()))
                    .addLast(new ProtobufEncoder())
                    .addLast(new ProtoBufServerHandler());
        }
    }

    private static class ProtoBufServerHandler extends SimpleChannelInboundHandler<DataInfo.Data> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Data msg) throws Exception {
            if (DataInfo.Data.DataType.StudentType.equals(msg.getDataType())) {
                DataInfo.Student student = msg.getStudent();
                System.out.println("收到了消息 : " + student);
            } else {
                DataInfo.Teacher teacher = msg.getTeacher();
                System.out.println("收到了消息 : " + teacher);
            }
        }
    }
}
