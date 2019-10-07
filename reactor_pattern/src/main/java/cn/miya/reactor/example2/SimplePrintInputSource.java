package cn.miya.reactor.example2;

import lombok.AllArgsConstructor;

/**
 * @author miya
 * @date 19-9-29
 */
@AllArgsConstructor
public class SimplePrintInputSource {

    private InputSource inputSource;

    public void print() {
        System.out.println("uuid = " + inputSource.getUuid() + ", data = " + inputSource.getData().toString());
    }

    public static void print(String eventType, InputSource inputSource) {
        System.out.println("eventType = " + eventType + ", uuid = " + inputSource.getUuid() + ", data = " + inputSource.getData().toString());
    }
}
