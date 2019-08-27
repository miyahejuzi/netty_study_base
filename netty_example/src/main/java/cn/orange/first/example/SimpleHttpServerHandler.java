package cn.orange.first.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
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





















