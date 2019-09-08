package cn.orange.apachethrift.example;

import cn.orange.apachethrift.generated.Person;
import cn.orange.apachethrift.generated.PersonService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * @author kz
 * @date 2019/8/30
 */
public class ThriftClient {

    public static void main(String[] args) {
        // 和客户端所用的 传输层和协议层 需要一致, 才能正确
        TTransport transport = new TFramedTransport(new TSocket("localhost", 8899), 600);
        TProtocol protocol = new TCompactProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);

        try {
            transport.open();
            Person person = client.getPersonByName("person");
            System.out.println("person = " + person);

            System.out.println("----------------");

            Person p2 = new Person();
            p2.setName("p2");
            p2.setAge(15);
            p2.setDescription("ren");
            client.savePerson(p2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }

}
