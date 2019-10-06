package cn.miya.reactor.example2;

import lombok.Getter;
import lombok.Setter;

/**
 * acceptor 处理 accept 事件
 *
 * @author miya
 * @date 19-9-29
 */
@Setter
@Getter
public class AcceptEventHandler extends AbstractEventHandler {

    private Selector selector;

    public AcceptEventHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void handler(Event event) {
        // 处理链接请求, 直接把这个链接请求升级成 EV_READ 事件
        if (event.getEventType() == EventType.EV_ACCEPT) {
            // printf data
            SimplePrintInputSource.print(event.getEventType().name(), event.getInputSource());

            Event readEvent = new Event();
            readEvent.setEventType(EventType.EV_READ);
            readEvent.setInputSource(event.getInputSource());
            selector.addEvent(readEvent);
        }
    }
}
