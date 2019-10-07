package cn.miya.reactor.example2;

/**
 * @author miya
 * @date 19-9-30
 */
public interface EventHandler {

    /**
     * 处理这个事件
     *
     * @param event event
     */
    void handler(Event event);
}
