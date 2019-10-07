package cn.orange.thread.prodcons;

import cn.orange.thread.utils.ThreadPoolFactory;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

/**
 * @author kz
 * @date 2019/9/13
 */

public class Storage {

    /**
     * ali 的规范约束必须要用手动创建的线程池, 所以我........
     */
    private ExecutorService cachedThreadPool = ThreadPoolFactory.getThreadPool(6, "thread");
    private static final int MAX_SIZE = 10;
    private final LinkedList<Object> list = new LinkedList<>();

    public void produce() throws InterruptedException {
        synchronized (list) {
            while (list.size() + 1 > MAX_SIZE) {
                System.out.println("【生产者" + Thread.currentThread().getName() + "】仓库已满");
                list.wait();
            }
            list.add(new Object());
            System.out.println("【生产者" + Thread.currentThread().getName() + "】生产一个产品，现库存" + list.size());
            list.notifyAll();
        }
    }

    public void consume() throws InterruptedException {
        synchronized (list) {
            while (list.size() == 0) {
                System.out.println("【消费者" + Thread.currentThread().getName() + "】仓库为空");
                list.wait();
            }
            list.remove();
            System.out.println("【消费者" + Thread.currentThread().getName() + "】消费一个产品，现库存" + list.size());
            list.notifyAll();
        }
    }

    private void doTest() {
        Storage storage = new Storage();
        for (int i = 0; i < 3; i++) {
            cachedThreadPool.execute(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        storage.produce();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            cachedThreadPool.execute(() -> {
                while (true) {
                    try {
                        Thread.sleep(3000);
                        storage.consume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        new Storage().doTest();
    }

}

