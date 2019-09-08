package cn.orange.nio;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author kz
 * @date 2019/9/7
 */
public class FourthTransferFrom {

    public static void main(String[] args) throws IOException {
        FileChannel readChannel = FileChannel.open(
                Paths.get("tmp.txt"), StandardOpenOption.READ);

        FileChannel writeChannel = FileChannel.open(
                Paths.get("tmp2.txt"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

//        直接复制
//        writeChannel.transferFrom(readChannel, 0, readChannel.size());

//        创建buffer 复制文件
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while(readChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            writeChannel.write(byteBuffer);
            byteBuffer.clear();
        }

    }

}
