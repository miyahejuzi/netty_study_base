package cn.orange.nio.example;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author kz
 * @date 2019/9/6
 */
public class FirstNioBufferExample {

    public static void main(String[] args) {
        // 指定缓冲区的容量
        IntBuffer buffer = IntBuffer.allocate(10);

        // 填充缓冲区
        for (int i = 0; i < buffer.capacity(); i++) {
            int anInt = new SecureRandom().nextInt(10);
            buffer.put(anInt);
        }

        // 让缓冲区可以被读
        buffer.flip();

        // 遍历缓冲区
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
