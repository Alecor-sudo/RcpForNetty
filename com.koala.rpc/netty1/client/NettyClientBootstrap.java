package netty1.client;

import netty1.service.DateTimeService;
import netty1.util.Constants;

/**
 * @author koala
 * @ClassName NettyClientBootstrap
 * @date 2020/7/30 13:44
 * @Description
 * @Version V1.0
 */

public class NettyClientBootstrap {

    public static void main(String[] args) {

        NettyClient nettyClient = new NettyClient(Constants.REMOTE_HOST, Constants.PORT);


        // 获取服务端对象的代理对象接口，
        Class serviceClass = DateTimeService.class;

        DateTimeService dateTimeService = (DateTimeService) nettyClient.getProxy(serviceClass);

        // 客户端通过代理对象来调用服务
        String result = dateTimeService.Hello("koala");

        // 输出通过代理调用调用服务器之后的响应结果
        System.out.println("Client Received Result From Server: [" + result + "]");

    }
}
