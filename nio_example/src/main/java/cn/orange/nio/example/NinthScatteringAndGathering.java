package cn.orange.nio.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.stream.Stream;

/**
 * @author kz
 * @date 2019/9/8
 */
public class NinthScatteringAndGathering {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(8899));

        long messageLen = 2 + 3 + 4;

        ByteBuffer[] buffers = new ByteBuffer[]{
                ByteBuffer.allocate(2),
                ByteBuffer.allocate(3),
                ByteBuffer.allocate(4)
        };

        SocketChannel socketChannel = channel.accept();

        // 循环读取数据, 然后返回
        // 值得注意的是, 读和写都是直接写的一个 ByteBuffer[]
        // 这就是 Scattering 和 Gathering
        while (true) {
            long byteReadLen = 0;

            while (byteReadLen < messageLen) {
                byteReadLen += socketChannel.read(buffers);

                Stream.of(buffers)
                        .map(buffer -> "position: " + buffer.position())
                        .forEach(System.out::println);
            }

            // flip(翻转)让 buffers 可以被读
            Stream.of(buffers).forEach(Buffer::flip);

            long byteWriteLen = 0;
            while (byteWriteLen < messageLen) {
                byteWriteLen += socketChannel.write(buffers);
            }

            // clear 让 buffers 可以被写入
            Stream.of(buffers).forEach(Buffer::clear);
        }

    }
}
