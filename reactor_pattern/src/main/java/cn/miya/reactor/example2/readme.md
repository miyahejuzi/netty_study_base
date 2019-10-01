
# 这个实现例子不是那么好

## 0x01 介绍
网上很多例子都是基于Java NIO 的一些类实现的网络通信的Reactor 模式, 
这个例子是很简单的, 主要是为了方便理解而实现的该模式. 
Reactor 模式毕竟不是只能用在 NIO 网络通信这个地方, 
很多非阻需求的业务场景都可以用到.

## 0x02 Reactor 解析 (基于某个给定的论文的标准 Reactor 模型)
// TODO 这里应该有一张图

- Initiation_Dispatcher
    - handler_event()
        ```java
          select(handlers);
          foreach h in handlers loop:
              h.handle_event(type)
          end loop
        ```
    - register_handler(h)
    - remove_handler(h) 
    
- Event_Handler
    - handler_event(type)
    - get_handle()
        - Concrete_Event_Handler
    
- Synchronous_Event_Demultiplexer
    - select()

- Handle

## 0x03 解释
- Event_Handler
    - 事件处理器, 处理特定的事件. 
    一般是一个接口, 应该被不同的事件处理器实现.

- Handle
    - 可以理解是事件的发生源, 比如 Socket, 
    他可以发生事件, selector 获取到事件并对应处理.

- Initiation_Dispatcher 
    - 用于管理 Event 和 EventHandler, 应该在初始化的时候就被注册
    - 并且持有 Event_Demultipexer 用于获取事件
    - netty 很多模型里会把 Event_Demultipexer 拆开成为 Acceptor 和 Other
    分别处理 accept 事件和 其他事件, netty 就是 bossGroup, workerGroup 两个线程池, 
    这样的话, 在对于很多 socket 同时链接服务器的时候, 不会丢请求.
    
- Event_Demultipexer
    - NIO 里面对应的就是 selector.
    - 具有一个 select() 方法, 可以阻塞等待事件的发生. 
    - 值得注意的是, 他应该不仅返回的发生了什么事件, 
    还要返回事件的发生源, 以便让 dispatcher 更好分发事件的对应处理. 
    比如 NIO Selector#select() 返回的是 SelectionKey对象, 
    对应获取 Channel. 
    
## 0x04 执行流程
- 由 Server 创建主要的程序, 初始化 InitiationDispatcher, Acceptor, Selector
- Acceptor 是一个单独的线程, 用于处理 accept 请求事件, 
- 当 EV_ACCEPT 来时, 就将 EV_ACCEPT 事件, 放入 Selector 内部的 eventQueue 里. 
- 然后 Dispatcher 调用 handleEvents() 就会收到 EV_ACCEPT 并处理这个事件.
- 处理事件会调用在初始化 Dispatcher 时往里面注册的 handlers, 
- (在网络模型里面, accept 之后, socket 会注册到 selector 里面, 并且selector会循环遍历 sockets, 获取事件)
    

### 别人的解释
```java
/*
 * 
 * 经典的网络服务在每个线程中完成对数据的处理：
 * 但这种模式在用户负载增加时，性能将下降非常的快。
 * 系统运行的性能瓶颈通常在I/O读写，包括对端口和文件的操作上，过去，在打 开一个I/O通道后，
 * read()将一直等待在端口一边读取字节内容，如果没有内容进来，read()也是傻傻的等，
 * 这会影响我们程序继续做其他事情，那 么改进做法就是开设线程，让线程去等待，但是这样做也是相当耗费资源（传统socket通讯服务器设计模式） 的。
 * 
 * Java NIO非堵塞技术实际是采取Reactor模式，或者说是Observer模式为我们监察I/O端口，
 * 如果有内容进来，会自动通知我们，这样，我们就不必开启多个线程死等，从外界看，实现了流畅的I/O读写，不堵塞了。 
 * NIO 有一个主要的类Selector,这个类似一个观察者 ，只要我们把需要探知的 socketchannel告诉Selector,
 * 我们接着做别的事情，当有事件发生时，他会通知我们，传回一组SelectionKey,我们读取这些 Key,就会获得我们刚刚注册过的socketchannel,
 * 然后，我们从这个Channel中读取数据，放心，包准能够读到，接着我们可以处理这些数据。 
 * Selector内部原理实际是在做一个对所注册的channel的轮询访问 ，不断的轮询(目前就这一个算法)，一旦轮询到一个channel有所注册的事情发生，
 * 比如数据来了，他就会站起来报告，交出一把钥匙，
 * 让我们通过这把钥匙（SelectionKey 表示 SelectableChannel 在 Selector 中的注册的标记。 ）来读取这个channel的内容。 
 * 
 * 反应器模式 
 * 用于解决多用户访问并发问题 
 * 举个例子：餐厅服务问题 
 * 传统线程池做法：来一个客人(请求)去一个服务员(线程) 
 * 反应器模式做法：当客人点菜的时候，服务员就可以去招呼其他客人了，等客人点好了菜，直接招呼一声“服务员” 
 */ 
```
