package com.jam2.bowebmanagementservice.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class StringCheckUtil {

    /**
     * null, empty, blank
     *
     */
    public Boolean isStringNEB(String string){

        if(null == string || string.isBlank() || string.isEmpty()){
            return true;
        }

        return false;
    }
}
