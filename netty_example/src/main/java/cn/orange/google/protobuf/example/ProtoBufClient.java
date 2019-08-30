package cn.orange.google.protobuf.example;

import cn.orange.google.protobuf.proto.DataInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * @author kz
 * @date 2019/8/29
 */
public class ProtoBufClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
        ) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ProtoBufClientInitializer());

            Channel channel = bootstrap.connect("localhost", 8899).sync().channel();

            while (true) {
                channel.writeAndFlush(br.readLine() + "\r\n");
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    private static class ProtoBufClientInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                    .addLast((new ProtobufVarint32LengthFieldPrepender()))
                    .addLast(new ProtobufDecoder(DataInfo.Data.getDefaultInstance()))
                    .addLast(new ProtobufEncoder())
                    .addLast(new ProtoBufClientHandler());
        }
    }

    private static class ProtoBufClientHandler extends SimpleChannelInboundHandler<DataInfo.Data> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            int num = new Random().nextInt(2);

            DataInfo.Data data;
            if (num == 0) {
                data = DataInfo.Data.newBuilder()
                        .setDataType(DataInfo.Data.DataType.StudentType)
                        .setStudent(DataInfo.Student.newBuilder()
                                .setName("student").setAddress("1984").setAge(18).build())
                        .build();
                ctx.channel().writeAndFlush(data);
            } else {
                data = DataInfo.Data.newBuilder()
                        .setDataType(DataInfo.Data.DataType.TeacherType)
                        .setTeacher(DataInfo.Teacher.newBuilder()
                                .setName("teacher").setWork("teacher").build())
                        .build();
                ctx.channel().writeAndFlush(data);
            }
            System.out.println("链接建立, 发送数据" + num);

        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Data msg) throws Exception {
            System.out.println(msg);
        }
    }
}
