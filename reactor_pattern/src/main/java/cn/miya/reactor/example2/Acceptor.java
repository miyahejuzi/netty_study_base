package cn.miya.reactor.example2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 处理链接请求, 单独一个线程来处理这件事情
 *
 * @author miya
 * @date 19-9-29
 */
public class Acceptor implements Runnable {

    private int port;

    private Selector selector;

    /**
     * 代表 serverSocket 模拟外部输入队列
     */
    private BlockingQueue<InputSource> sourceQueue = new LinkedBlockingQueue<>();

    public Acceptor(int port, Selector selector) {
        this.port = port;
        this.selector = selector;
    }

    public int getPort() {
        return port;
    }

    public void connection(InputSource inputSource) {
        sourceQueue.offer(inputSource);
    }

    @Override
    public void run() {
        while (true) {
            InputSource source = null;
            try {
                // 相当于 serverSocket.accept(), 接收输入请求，该例从请求队列中获取输入请求
                source = sourceQueue.take();
            } catch (InterruptedException e) {
                // ignore it;
            }

            //接收到 InputSource 后将接收到 event 设置 type 为 ACCEPT, 并将 source 赋值给 event
            if (source != null) {
                Event acceptEvent = new Event();
                acceptEvent.setInputSource(source);
                acceptEvent.setEventType(EventType.EV_ACCEPT);
                selector.addEvent(acceptEvent);
            }
        }
    }
}
