package netty1.test;

import netty1.client.NettyClient;
import netty1.service.DateTimeService;
import netty1.util.Constants;
import sun.jvm.hotspot.runtime.Threads;

import java.util.concurrent.CountDownLatch;

/**
 * @author koala
 * @ClassName ClientDemo
 * @date 2020/8/1 08:55
 * @Description
 * @Version V1.0
 */

public class ClientDemo {


    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        int thread_count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);

        for (int i=0;i<thread_count;i++){
            new Thread(() -> {

                NettyClient nettyClient = new NettyClient(Constants.REMOTE_HOST, Constants.PORT);


                // 获取服务端对象的代理对象接口，
                Class serviceClass = DateTimeService.class;

                DateTimeService dateTimeService = (DateTimeService) nettyClient.getProxy(serviceClass);

                String stringData = "koala-" + Thread.currentThread().getName();

                // 客户端通过代理对象来调用服务
                String result = dateTimeService.Hello(stringData);


                // 输出通过代理调用调用服务器之后的响应结果
                System.out.println(stringData + "Client Received Result From Server: [" + result + "]");

                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("线程数："+ thread_count+",执行时间:" + (end-start));
    }

}
