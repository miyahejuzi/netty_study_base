package cn.orange.google.protobuf;

import cn.orange.google.protobuf.proto.DataInfo;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author kz
 * @date 2019/8/29
 */
public class ProtoBufSimpleTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        DataInfo.Student student = DataInfo.Student.newBuilder().setName("hello").setAddress("1984").setAge(18).build();

        byte[] bytes = student.toByteArray();

        DataInfo.Student s2 = DataInfo.Student.parseFrom(bytes);

        System.out.println("s2 = " + s2);
    }

}
