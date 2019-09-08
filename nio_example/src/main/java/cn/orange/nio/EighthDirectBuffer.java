package cn.orange.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author kz
 * @date 2019/9/7
 */
public class EighthDirectBuffer {

    public static void main(String[] args) throws IOException {

        FileChannel readChannel = FileChannel.open(
                Paths.get("tmp.txt"), StandardOpenOption.READ);

        FileChannel writeChannel = FileChannel.open(
                Paths.get("tmp2.txt"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        // DirectBuffer
        // native heap 这样少了一次内存复制, 也就是 zero copy
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);

        while (readChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            writeChannel.write(byteBuffer);
            byteBuffer.clear();
        }
    }
}
