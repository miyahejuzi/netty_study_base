package cn.orange.nio.niocliser.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kz
 * @date 2019/9/10
 */
public class NioTcpServer extends BaseTcpSelector {

    private ServerSocketChannel channel;

    private List<SocketChannel> socketChannels = new ArrayList<>();

    public NioTcpServer(int port) {
        super();
        try {
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            registerChannel(channel, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doOnSelectionKey(SelectionKey key) {
        if (key.isAcceptable()) {
            try {
                SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                socketChannels.add(socketChannel);
                System.out.println("server find client : " + socketChannel.getRemoteAddress());
                registerChannel(socketChannel, SelectionKey.OP_READ);
                write(socketChannel.getRemoteAddress() + " is logged in");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 说明连接已经断开
            int len = 0;
            try {
                len = socketChannel.read(readBuffer);
            } catch (IOException e) {
                try {
                    socketChannel.close();
                    socketChannels.remove(socketChannel);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                if (len == -1) {
                    System.out.println("client read : len = -1");
                    socketChannel.close();
                    socketChannels.remove(socketChannel);
                } else {
                    readBuffer.flip();
                    byte[] buffer = new byte[len];
                    readBuffer.get(buffer, 0, buffer.length);
                    readBuffer.clear();
                    String str = new String(buffer);
                    System.out.println("server read : len=" + buffer.length + ", str=" + str);
                    write(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(String str) {
        writeBuffer.put(str.getBytes());
        writeBuffer.flip();
        System.out.println("server write : len=" + str.length() + ", str=" + str);
        for (SocketChannel channel : socketChannels) {
            try {
                channel.write(writeBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeBuffer.rewind();
        }
        writeBuffer.clear();
    }

    public void close() {
        System.out.println("close server");
        super.closeSelector();
        for (SocketChannel channel : socketChannels) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socketChannels.clear();
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioTcpServer nioTcpServer = new NioTcpServer(8899);
        nioTcpServer.select();
    }
}
