package cn.orange.nio.niocliser.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author kz
 * @date 2019/9/9
 */
public class Server {
    private static final int BUF_SIZE = 1024;

    public void start(int port) {
        try (
                // 开启一个 channel
                ServerSocketChannel socketChannel = ServerSocketChannel.open();
                // 开启一个 selector
                Selector selector = Selector.open()
        ) {
            socketChannel.socket().bind(new InetSocketAddress(port));
            socketChannel.configureBlocking(false);
            // 将通道注册到一个selector上, 添加 accept 事件感兴趣
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 获取所有的keySet用于对channel进行处理
            while (selector.select() > 0) {
                // 使用 for (SelectionKey key : selector.selectedKeys()) 方式无法移除对象
                // Iterator对象的remove方法是迭代过程中删除元素的唯一方法
                // 如果在处理完对象后不移除对象会有错误
                Iterator<SelectionKey> selectKeys = selector.selectedKeys().iterator();
                while (selectKeys.hasNext()) {
                    SelectionKey key = selectKeys.next();
                    //分别处理接受、连接、读、写4中状态
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                    if (key.isWritable() && key.isValid()) {
                        handleWrite(key);
                    }
                    if (key.isConnectable()) {
                        System.out.println("isConnectable = true");
                    }
                    selectKeys.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleWrite(SelectionKey key) {
        // do nothing
    }

    private void handleAccept(SelectionKey key) throws IOException {
        // 获取ServerSocket的Channel
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        // 监听新进来的链接
        SocketChannel sc = ssChannel.accept();
        // 设置client链接为非阻塞
        sc.configureBlocking(false);
        // 将channel注册Selector, 添加读事件感兴趣
        sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(BUF_SIZE));
    }

    private void handleRead(SelectionKey key) throws IOException {
        // 获取链接client的channel
        SocketChannel channel = (SocketChannel) key.channel();
        // 获取附加在key上的数据
        ByteBuffer buf = (ByteBuffer) key.attachment();
        // 读channel上数据到buf
        while (channel.read(buf) > 0) {
            buf.flip();
            // 输出 buf 上的数据
            CharBuffer decode = Charset.forName("UTF-8").decode(buf);
            System.out.println(decode.toString());
            buf.clear();
        }
    }

    public static void main(String[] args) {
        new Server().start(8899);
    }
}

