package cn.orange.nio;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author kz
 * @date 2019/9/6
 */
public class ThirdNioChannelWriteExample {

    public static void main(String[] args) throws IOException, URISyntaxException {
//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("tmp.txt"));
//        bos.write("hello world".getBytes());
//        bos.flush();
//        bos.close();

        FileChannel channel = FileChannel.open(Paths.get(
                ClassLoader.getSystemResource("tmp.txt").toURI()), StandardOpenOption.WRITE);
//        FileChannel channel = FileChannel.open(Paths.get("tmp.txt"), StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        byte[] message = "hello world".getBytes();
        byteBuffer.put(message);

        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.close();


    }
}