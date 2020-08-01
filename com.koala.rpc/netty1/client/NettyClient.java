package netty1.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author koala
 * @ClassName NettyClient
 * @date 2020/7/30 12:50
 * @Description netty 客户端
 * @Version V1.0
 */

public class NettyClient {

    // 主机名称，端口号
    private String hostname;
    private int port;

    public NettyClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;

        // 初始化 NettyClient 对象的时候，就启动 NettyClient
        clientStart();
    }

    private NettyClientHandler clientHandler;

    /**
     * 当前系统可用线程数
     */
    private static int cpu_cores = Runtime.getRuntime().availableProcessors();

    /**
     * 初始化线程池
     */
    private static ExecutorService threadPool = Executors.newFixedThreadPool(cpu_cores);

    private void clientStart() {

        // 事件线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        clientHandler = new NettyClientHandler();

        // 客户端启动类
        Bootstrap client = new Bootstrap();

        client.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                       ChannelPipeline pipeline =  channel.pipeline();
                       pipeline
                               .addLast(new StringEncoder()) //编码
                               .addLast(new StringDecoder()) // 解码
                               .addLast(clientHandler);  // 添加业务处理的类
                    }
                });

        try {
            // 连接
            client.connect(hostname,port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * 代理类，客户端通过它去调用服务端的方法
     * @param serviceClass
     * @return
     */
    public Object getProxy(final Class<?> serviceClass){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                System.out.println("客户端发送的请求参数是：" + args[0].toString());

                //客户端调用发送请求
                clientHandler.setRequestParam(args[0].toString());

                // 通过线程调用获取反馈
                return threadPool.submit(clientHandler).get();

            }
        });
    }


}
