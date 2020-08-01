package netty1.service;


import java.util.Date;

/**
 * @author koala
 * @date 2020/7/30
 */

public interface DateTimeService {

    String Hello(String name);


    // 获取当前时间
    String getNow();


    String format(String format,  Date date);

    
    String format(Date date);

}
