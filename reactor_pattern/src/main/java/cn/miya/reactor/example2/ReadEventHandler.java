package cn.miya.reactor.example2;

/**
 * @author miya
 * @date 19-9-30
 */
public class ReadEventHandler extends AbstractEventHandler {

    @Override
    public void handler(Event event) {
        SimplePrintInputSource.print(event.getEventType().name(), event.getInputSource());
    }
}
