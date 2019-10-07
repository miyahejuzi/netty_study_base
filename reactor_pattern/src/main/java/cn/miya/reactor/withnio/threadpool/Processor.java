package cn.miya.reactor.withnio.threadpool;

import cn.miya.reactor.utils.ThreadPoolFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.*;

/**
 * @author miya
 */
public class Processor {

    private static final ExecutorService SERVICE = ThreadPoolFactory.getThreadPool(4, "processor");

    public void process(SelectionKey selectionKey) {
        SERVICE.submit(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int count = socketChannel.read(buffer);
            if (count < 0) {
                socketChannel.close();
                selectionKey.cancel();
                System.out.println("读取结束！");
            } else if (count == 0) {
                return null;
            }
            buffer.flip();
            final CharBuffer decode = Charset.forName("UTF-8").decode(buffer);
            System.out.println("读取内容: " + decode.toString());
            return null;
        });
    }
}