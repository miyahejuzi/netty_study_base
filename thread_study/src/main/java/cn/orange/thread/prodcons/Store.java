package cn.orange.thread.prodcons;

/**
 * @author kz
 * @date 2019/9/12
 */
public class Store {

    private static final int MAX_NUM = 3;
    private static final int MIN_MUN = 0;

    private int total = 0;

    public static void main(String[] args) {
        Store store = new Store();
        new Thread(store::produces).start();
        new Thread(store::produces).start();
        new Thread(store::produces).start();
        new Thread(store::consumes).start();
        new Thread(store::consumes).start();
        new Thread(store::consumes).start();
    }

    public void produces() {
        while (true) {
            synchronized (this) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isFull()) {
                    // 没有满就生产东西
                    ++total;
                    System.out.println(Thread.currentThread().getName() + " 生成一个产品, 目前有: " + total + "个");
                } else {
                    // 满了就自己睡觉, 并把别人叫醒
                    try {
                        notifyAll();
                        // wait() 必须在同步方法块里调用, 且必须由锁调用
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void consumes() {
        while (true) {
            synchronized (this) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isEmpty()) {
                    --total;
                    System.out.println(Thread.currentThread().getName() + " 消费一个产品, 目前剩余有: " + total + "个");
                } else {
                    try {
                        notifyAll();
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean isFull() {
        return total >= MAX_NUM;
    }

    private boolean isEmpty() {
        return total <= MIN_MUN;
    }

}
