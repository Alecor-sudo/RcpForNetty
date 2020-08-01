package netty1.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty1.service.impl.DateTimeServiceImpl;

/**
 * @author koala
 * @ClassName NettyServerHandle
 * @date 2020/7/30 13:44
 * @Description
 * @Version V1.0
 */

public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx)   {
        System.out.println("客户端连接成功!"+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)   {
        System.out.println("客户端断开连接!{}" + ctx.channel().remoteAddress());
        ctx.channel().close();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        System.out.println("服务端接收到消息: [" + msg + "]");


        // 处理参数
        String name = msg.toString();

        // 先做一个最简单的服务实现 处理相关业务实现
        String result = new DateTimeServiceImpl().Hello(name);

        // 返回业务处理结果
        ctx.writeAndFlush(result);
    }

    /**
     * 当出现异常进行调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常。断开连接");
        ctx.close();

    }
}
