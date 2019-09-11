package cn.orange.nio.example;

import java.nio.ByteBuffer;

/**
 * @author kz
 * @date 2019/9/7
 */
public class FifthCustomProtocol {

    /**
     * 数据按照一定的格式存放进去, 然后也可以按照一定格式的读取出来
     * 这样就是可以一种自定义协议(custom protocol)的进行通信/数据传输
     */
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        byteBuffer.putInt(10);
        byteBuffer.putChar('我');
        byteBuffer.putDouble(18.1);

        byteBuffer.flip();
        // 放进去和取出来的不一样就会出错, 0.0
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getDouble());

    }

}
