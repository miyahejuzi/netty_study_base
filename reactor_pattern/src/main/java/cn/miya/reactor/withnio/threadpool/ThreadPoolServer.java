package cn.miya.reactor.withnio.threadpool;

import lombok.extern.slf4j.Slf4j;

import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @author miya
 * @date 19-10-3
 */
@Slf4j
public class ThreadPoolServer {
    private static final int BUF_SIZE = 1024;

    private Selector selector;

    public ThreadPoolServer(int port) {
        try {
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("server start...");
        try {
            while (selector.select() > 0) {
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach((key) -> {
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                });
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) {
        ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel socketChannel = acceptServerSocketChannel.accept();
            socketChannel.configureBlocking(false);
            SelectionKey registerKey = socketChannel.register(selector, SelectionKey.OP_READ);
            //绑定处理器线程
            registerKey.attach(new Processor());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[server]接收到新的链接");
    }

    private void handleRead(SelectionKey key) {
        ((Processor) key.attachment()).process(key);
    }

    public static void main(String[] args) {
        new ThreadPoolServer(8899).start();
    }
}

















