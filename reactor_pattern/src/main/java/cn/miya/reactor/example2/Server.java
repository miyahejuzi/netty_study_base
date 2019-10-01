package cn.miya.reactor.example2;

import java.util.Scanner;

/**
 * @author miya
 * @date 19-9-29
 */
public class Server {

    private Selector selector = new Selector();

    private InitiationDispatcher eventLooper = new InitiationDispatcher(selector);

    private Acceptor acceptor;

    Server(int port) {
        acceptor = new Acceptor(port, selector);
    }

    public Acceptor getAcceptor() {
        return acceptor;
    }

    /**
     * 启动了两个线程
     * 1, eventLooper, 处理通过 selector#select() 获得的事件对象
     * 2, acceptor, 处理 accept 事件请求
     */
    public void start() {
        // 添加 accept 事件, 和处理器
        eventLooper.regist(EventType.EV_ACCEPT, new AcceptEventHandler(selector));
        eventLooper.regist(EventType.EV_READ, new ReadEventHandler());

        new Thread(acceptor, "Acceptor-" + acceptor.getPort()).start();
        eventLooper.handleEvents();
    }

    public static void main(String[] args) {
        Server server = new Server(8899);

        new Thread(server::start, "EventLooper-1").start();

        Scanner sc = new Scanner(System.in);
        // 模拟 client 链接
        while (true) {
            String s = sc.nextLine();
            InputSource inputSource = new InputSource();
            inputSource.setData(s);
            server.getAcceptor().connection(inputSource);
        }
    }

}
