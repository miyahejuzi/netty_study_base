package cn.orange.nio.cliser.base;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @author kz
 * @date 2019/9/10
 */
public abstract class BaseTcpSelector {

    private static final int BUF_SIZE = 1024;

    private Selector selector;
    ByteBuffer readBuffer = ByteBuffer.allocate(BUF_SIZE);
    ByteBuffer writeBuffer = ByteBuffer.allocate(BUF_SIZE);

    protected BaseTcpSelector() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void registerChannel(SelectableChannel channel, int ops) {
        try {
            channel.configureBlocking(false);
            channel.register(selector, ops);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void select() {
        // 抛出异常后直接终止程序
        try {
            while (true) {
                if (!selector.isOpen()) {
                    System.out.println("selector is closed");
                    break;
                }
                if (selector.select() <= 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isValid()) {
                        doOnSelectionKey(key);
                    }
                    iterator.remove();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现此方法用于处理
     * * if (key.isAcceptable()) {
     * } else if (key.isConnectable()) {
     * } else if (key.isReadable()) {
     * } else if (key.isWritable()) {
     * } 情况,
     *
     * @param key SelectionKey
     */
    protected abstract void doOnSelectionKey(SelectionKey key);

    protected void closeSelector() {
        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
