package cn.orange.nio.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author kz
 * @date 2019/9/6
 */
public class SecondNioChannelExample {

    public static void main(String[] args) throws IOException, URISyntaxException {
        // new FileInputStream("tmp.txt").getChannel();
        FileChannel channel = FileChannel.open(Paths.get(
                ClassLoader.getSystemResource("tmp.txt").toURI()), StandardOpenOption.READ);
        // ClassLoader.getSystemResource("tmp.txt").toURI() 可以获取到classpath目录下的文件路径
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        channel.read(byteBuffer);
        byteBuffer.flip();

        while(byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            System.out.println("Charactor: " + (char) b);
        }

        channel.close();

    }
}