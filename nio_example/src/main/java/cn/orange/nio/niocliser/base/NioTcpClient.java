package cn.orange.nio.niocliser.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author kz
 * @date 2019/9/11
 */
public class NioTcpClient extends BaseTcpSelector {

    private SocketChannel socketChannel;
    private boolean isConnected;

    public NioTcpClient(String hostname, int port) {
        super();
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(hostname, port));
            registerChannel(socketChannel, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void doOnSelectionKey(SelectionKey key) {
        if (key.isAcceptable()) {
            System.out.println("client is accepted by server");
        } else if (key.isConnectable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            if (channel.isConnectionPending()) {
                try {
                    if (channel.finishConnect()) {
                        System.out.println("client connect server success");
                        isConnected = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (key.isReadable()) {
            try {
                int len = socketChannel.read(readBuffer);
                if (len == -1) {
                    System.out.println("client read : len=-1");
                    close();
                } else {
                    readBuffer.flip();
                    byte[] buffer = new byte[len];
                    readBuffer.get(buffer);
                    readBuffer.clear();
                    System.out.println("client read : len=" + len + ", str=" + new String(buffer));
                }
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }
    }

    private void write(String str) {
        writeBuffer.put(str.getBytes());
        writeBuffer.flip();
        try {
            System.out.println("client write : len=" + str.length() + ", str=" + str);
            socketChannel.write(writeBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeBuffer.clear();
    }

    public void close() {
        System.out.println("close client");
        isConnected = false;
        super.closeSelector();
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioTcpClient nioTcpClient = new NioTcpClient("127.0.0.1", 8899);
        new Thread(nioTcpClient::select).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            nioTcpClient.write(scanner.nextLine());
        }
    }

}
