package cn.miya.reactor.withnio.acceptor;

import cn.miya.reactor.utils.ThreadPoolFactory;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author miya
 */
public class Processor {

    private static final ExecutorService SERVICE = ThreadPoolFactory.getThreadPool(4, "Processor");

    private Selector selector;

    public Processor() {
        try {
            this.selector = SelectorProvider.provider().openSelector();
        } catch (IOException e) {
            e.printStackTrace();
        }
        process();
    }

    public void registerChannel(SocketChannel socketChannel) {
        try {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        System.out.println("sub selector 注册 socket channel 成功...");
    }

    /**
     * netty 里面是:
     * 分配一个线程给 sub selector 查询 socket 状态,
     * 然后将其他线程分配给处理线程
     */
    public void process() {
        SERVICE.submit(() -> {
            while (true) {
                if (selector.selectNow() <= 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach((key) -> {
                    if (key.isReadable()) {
                        process(key);
                    }
                });
                selectionKeys.clear();
            }
        });
    }

    /**
     * 分配线程处理读取任务
     */
    public void process(SelectionKey selectionKey) {
        SERVICE.submit(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int count = socketChannel.read(buffer);
            // count < 0 表示断开链接
            if (count < 0) {
                socketChannel.close();
                selectionKey.cancel();
                System.out.println("读取结束！");
                return null;
            } else if (count > 0) {
                buffer.flip();
                final CharBuffer decode = Charset.forName("UTF-8").decode(buffer);
                System.out.println("读取内容: " + decode.toString());
                return null;
            }
            return null;
        });
    }
}