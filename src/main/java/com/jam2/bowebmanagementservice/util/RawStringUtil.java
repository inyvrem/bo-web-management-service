package com.jam2.bowebmanagementservice.util;

import org.springframework.stereotype.Component;

@Component
public class RawStringUtil {

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

    public String convertToValidUUID(String raw){

        if (raw.length() != 32) {
            throw new IllegalArgumentException("Invalid length for UUID string.");
        }

        return raw.substring(0, 8) + "-" +
                raw.substring(8, 12) + "-" +
                raw.substring(12, 16) + "-" +
                raw.substring(16, 20) + "-" +
                raw.substring(20);

    }
}
