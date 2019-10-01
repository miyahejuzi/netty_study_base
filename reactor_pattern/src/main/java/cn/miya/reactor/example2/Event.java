package cn.miya.reactor.example2;

import lombok.Data;

/**
 * @author miya
 * @date 19-9-29
 */
public class Event {

    private InputSource inputSource;

    private EventType eventType;

    public InputSource getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
