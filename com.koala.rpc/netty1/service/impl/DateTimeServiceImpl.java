package netty1.service.impl;

import netty1.service.DateTimeService;
import io.netty.util.internal.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author koala
 * @ClassName DateTimeServiceImpl
 * @date 2020/7/30 13:23
 * @Description
 * @Version V1.0
 */

public class DateTimeServiceImpl implements DateTimeService {


    private String DATE_FORMAT = "yyyy-mm-dd hh:mm:ss";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public String Hello(String name) {
         return  "I am koala ,我在"+ getNow() + "给你发消息";
    }

    @Override
    public String getNow() {
        return simpleDateFormat.format(new Date());
    }

    @Override
    public String format(String format, Date date) {
        if (StringUtil.isNullOrEmpty(format)) {
            return simpleDateFormat.format(date);
        } else {
            SimpleDateFormat sdf1 = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        }
    }

    @Override
    public String format(Date date) {
        return simpleDateFormat.format(date);
    }
}
