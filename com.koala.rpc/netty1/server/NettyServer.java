package netty1.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * nettyserver
 *
 * @author koala
 * @ClassName NettyServer
 * @date 2020/7/30 12:38
 * @Description netty 服务端
 * @Version V1.0
 */

public class NettyServer {


    // 主机名称，端口号
    private String hostname;
    private int port;

    public NettyServer(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * 对外公开的方法
     */
    public void serverStart() {
        serverStart0(hostname, port);
    }

    private void serverStart0(String hostname, int port) {

        // 用于处理Accept的事件线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        // 处理  read、write这些操作的的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

        ServerBootstrap server = new ServerBootstrap();


        server.group(bossGroup, workerGroup).
                channel(NioServerSocketChannel.class).  // 通道协议，这里使用Socket，同样也支持Tcp：NioSctpServerChannel
                childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline
                                .addLast(new StringEncoder())  // 编码器
                                .addLast(new StringDecoder())  // 解码器
                                .addLast(new NettyServerHandler());     // 此处添加业务处理Handler
                    }
                });


        try {
            // 异步启动， 如果启动完之后，释放当前线程
            ChannelFuture channelFuture = server.bind(hostname, port).sync();

            System.out.println("NettyServer is started ..... ");

            // 等待服务端端口监听关闭,（Server线程状态变为waitting，等待子线程退出））
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
