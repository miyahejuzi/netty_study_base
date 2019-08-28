package cn.orange.httpserver.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 1. 添加了handler的生命周期的方法
 *
 * @author kz
 * @date 2019/8/27
 */
public class SimpleHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /***
     * 读取客户端发过来的真正的请求, 并且向客户端返回响应的一个方法
     * -- 后续在进行扩展
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.print("uri = " + request.uri() + "\t method = " + request.method().name());
            System.out.println();

            // 设置要返回的内容
            ByteBuf content = Unpooled.copiedBuffer("hello world!", CharsetUtil.UTF_8);
            // 指定按照什么格式返回
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response
                    .headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, "text/html")
                    .set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            // 返回响应
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }
}





















