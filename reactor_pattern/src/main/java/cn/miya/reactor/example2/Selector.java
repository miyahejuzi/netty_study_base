package cn.miya.reactor.example2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author miya
 * @date 19-9-29
 */
public class Selector {

    private BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();

    private final Object lock = new Object();

    /**
     * 向 queue 里面添加事件, 这个锁是为了和下面的 select() 里对应
     * 如果添加成功(队列不为空), 同时能获取到锁(可能在lock.wait()) 就唤醒这个线程
     *
     * @param event event
     */
    public void addEvent(Event event) {
        if (eventQueue.offer(event)) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    public List<Event> select() {
        return select(0);
    }

    public List<Event> select(long timeout) {
        if (timeout > 0) {
            // 双重判断
            if (eventQueue.isEmpty()) {
                synchronized (lock) {
                    if (eventQueue.isEmpty()) {
                        try {
                            lock.wait(timeout);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }
                }
            }
        }

        // 直接返回所有的事件
        List<Event> events = new ArrayList<Event>();
        eventQueue.drainTo(events);
        return events;
    }

}
