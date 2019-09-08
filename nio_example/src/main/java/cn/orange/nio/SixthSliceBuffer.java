package cn.orange.nio;

import java.nio.ByteBuffer;

/**
 * @author kz
 * @date 2019/9/7
 */
public class SixthSliceBuffer {

    /**
     * 截取原来的 buffer, 但实际上是新截出来的和原来的, 共用的一块东西;
     */
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        for (int i = 0; i < 10; i++) {
            byteBuffer.put((byte) i);
        }

        byteBuffer.position(3).limit(6);
        // slice 包含原 buffer 里面的 [3, 6) 的元素
        ByteBuffer slice = byteBuffer.slice();

        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get();
            b *= 2;
            slice.put(i, b);
        }

        byteBuffer.clear();

        // 可以看到原 buffer 里面的内容 [3, 6) 索引的内容已经被改变了
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }

    }
}
