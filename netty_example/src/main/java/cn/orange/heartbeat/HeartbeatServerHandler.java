package cn.orange.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author kz
 * @date 2019/8/29
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType;
            switch (event.state()) {
                case ALL_IDLE:
                    eventType = "all idle";
                    break;
                case READER_IDLE:
                    eventType = "reader idle";
                    break;
                case WRITER_IDLE:
                    eventType = "writer idle";
                    break;
                default:
                    eventType = "There is a logical error";
            }
            System.out.println("eventTpye = " + eventType);
            ctx.channel().close();
        }
    }
}
