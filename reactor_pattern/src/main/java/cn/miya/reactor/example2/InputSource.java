package cn.miya.reactor.example2;

import java.util.UUID;

/**
 * @author miya
 * @date 19-9-29
 */
public class InputSource {

    private String uuid = UUID.randomUUID().toString();

    private Object data;

    public String getUuid() {
        return uuid;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
