package cn.orange.nio.example;

import java.nio.ByteBuffer;

/**
 * @author kz
 * @date 2019/9/7
 */
public class SeventhOnlyReadBuffer {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        for (int i = 0; i < 10; i++) {
            byteBuffer.put((byte) i);
        }

        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

//        如果尝试修改, 就会抛出 ReadOnlyBufferException 异常;
//        readOnlyBuffer.put((byte) 1);
    }

}
