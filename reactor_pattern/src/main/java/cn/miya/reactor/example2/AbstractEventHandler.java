package cn.miya.reactor.example2;

import lombok.Getter;
import lombok.Setter;

/**
 * 事件处理的抽象类
 *
 * @author miya
 * @date 19-9-29
 */
@Getter
@Setter
public abstract class AbstractEventHandler implements EventHandler {

    private InputSource inputSource;

    /**
     * 处理这个事件
     *
     * @param event event
     */
    @Override
    public abstract void handler(Event event);
}
