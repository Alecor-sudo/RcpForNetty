package netty1.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author koala
 * @ClassName NettyClientHandler
 * @date 2020/7/30 14:05
 * @Description 客户端的通道处理类，实现Callable是用于支持多线程
 * @Version V1.0
 */

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {


    // 上下文对象，因为将来幺在call方法中使用
    private ChannelHandlerContext context;

    // 存放结果
    private String result;


    // 存放请求的一些参数
    private String requestParam;

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    @Override
    public synchronized Object call() throws Exception{

        System.out.println(Thread.currentThread().getName() + "NettyClientHandler call() Invoacated ... ");

        // 发送请求
        context.writeAndFlush(requestParam);

        // 发送请求完之后，阻塞，等待服务端发回来数据
        wait();

        // 如果没唤醒了，就继续处理
        return result;

    }


    /**
     * 当客户端通道激活时候
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(" 客户端 channelActive 被调用  ");
        // 向服务端发送请求。
        this.context = ctx;

    }

    /**
     *
     * @param ctx
     * @param msg
     * @throws Exception
     * channelRead 当收到服务端消息的时候，用来处理服务端返回来的数据。
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        System.out.println("Client received result: " + result);
        // 之前在call方法里面阻塞了当前线程，当收到服务端的消息时候，唤醒线程，就可以将消息再次发给服务端
        // 如果去掉也可以，一并给上面的wait去掉
        notify();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
