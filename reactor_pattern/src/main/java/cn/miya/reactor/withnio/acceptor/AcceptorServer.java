package cn.miya.reactor.withnio.acceptor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author miya
 * @date 19-10-3
 */
@Slf4j
public class AcceptorServer {
    private static final int BUF_SIZE = 1024;

    private Selector selector;

    private Processor processor;

    public AcceptorServer(int port) {
        this.processor = new Processor();
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
                });
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) {
        System.out.println("[server]接收到新的链接");
        ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel socketChannel = acceptServerSocketChannel.accept();
            socketChannel.configureBlocking(false);
            // 绑定到 subSelector
            processor.registerChannel(socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AcceptorServer(8899).start();
    }
}

















