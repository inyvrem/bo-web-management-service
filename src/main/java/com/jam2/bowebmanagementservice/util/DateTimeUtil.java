package com.jam2.bowebmanagementservice.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateTimeUtil {
    public Date now() {
        return new Date();
    }

    public String DmyDateTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        return format.format(date);
    }
}
