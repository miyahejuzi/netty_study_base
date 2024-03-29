package cn.orange.apachethrift.example;

import cn.orange.apachethrift.generated.PersonService;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * @author kz
 * @date 2019/8/30
 */
public class ThriftServer {

    public static void main(String[] args) throws TTransportException {
        // 客户端和服务端链接的对象
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        // 半同步半异步 half sync, 传输协议必须用 帧格式协议
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        // 协议层
        arg.protocolFactory(new TCompactProtocol.Factory());
        // 传输层所用的对象
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));

        TServer ts = new THsHaServer(arg);
        System.out.println("服务已启动");
        ts.serve();
    }
}
