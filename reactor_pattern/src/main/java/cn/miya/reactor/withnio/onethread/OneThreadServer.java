package cn.miya.reactor.withnio.onethread;

import cn.miya.reactor.example2.Server;
import lombok.extern.slf4j.Slf4j;

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
 * 单线程的 reactor 模式, 基于 nio 实现
 * 整体来说, 其实和 nio 简单实现网络通信是一样的
 *
 * @author miya
 * @date 19-10-3
 */
@Slf4j
public class OneThreadServer {
    private static final int BUF_SIZE = 1024;

    private Selector selector;

    public OneThreadServer(int port) {
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
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel sc = ssChannel.accept();
            sc.configureBlocking(false);
            sc.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRead(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);
        try {
            while (channel.read(buf) > 0) {
                buf.flip();
                // 输出 buf 上的数据
                CharBuffer decode = Charset.forName("UTF-8").decode(buf);
                System.out.println(decode.toString());
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new OneThreadServer(8899).start();
    }
}

















