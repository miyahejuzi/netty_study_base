package cn.miya.reactor.withnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author kz
 * @date 2019/9/9
 */
public class Client {

    public void start(String address, int port) {
        try (
                SocketChannel socketChannel = SocketChannel.open();
                Selector selector = Selector.open()
        ) {
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(address, port));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            // 获取所有的keySet用于对channel进行处理
            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isConnectable()) {
                        handlerConnect(key);
                    } else if (key.isReadable()) {
                        handlerRead(key);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlerConnect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        // 判断这个链接时候是否挂起的
        if (channel.isConnectionPending()) {
            // 让这个链接建立好
            channel.finishConnect();
        }
        channel.configureBlocking(false);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        writeBuffer.put("向服务端发送了一条信息".getBytes());
        writeBuffer.flip();
        channel.write(writeBuffer);
        channel.register(key.selector(), SelectionKey.OP_READ);
    }

    private void handlerRead(SelectionKey key) {
        // TODO
    }

    public static void main(String[] args) {
        new Client().start("localhost", 8899);
    }
}

