package netty1.server;

import netty1.util.Constants;

/**
 * @author koala
 * @ClassName NettyServerBootstrap
 * @date 2020/7/30 13:42
 * @Description
 * @Version V1.0
 */

public class NettyServerBootstrap {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer(Constants.REMOTE_HOST,Constants.PORT);


        // 启动服务
        nettyServer.serverStart();
    }
}
