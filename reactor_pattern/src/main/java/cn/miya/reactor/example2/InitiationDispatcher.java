package cn.miya.reactor.example2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化分发器, 用于将指定的事件响应给对应的处理器
 *
 * @author miya
 * @date 19-9-29
 */
public class InitiationDispatcher {

    private Map<EventType, EventHandler> eventHandlerMap = new ConcurrentHashMap<>();

    private Selector selector;

    public InitiationDispatcher(Selector selector) {
        this.selector = selector;
    }

    public void regist(EventType eventType, EventHandler handler) {
        eventHandlerMap.put(eventType, handler);
    }

    public void handleEvents() {
        dispatch();
    }

    private void dispatch() {
        while (true) {
            selector.select().forEach((event -> eventHandlerMap.get(event.getEventType()).handler(event)));
        }
    }

}
