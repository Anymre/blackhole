package com.mark.web;

import com.alibaba.nacos.api.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Marcus lv
 * @date 2020/9/14 17:44
 */
class WebApplicationTest {
    
    @Test
    void tests() throws UnsupportedEncodingException {
        String target = "+++---";
        target = URLEncoder.encode(target, "GBK");
        System.out.println(target);
        target = innerDecode(null, target, "UTF-8");
        System.out.println(target);
        
    }
    
    private static String innerDecode(String pre, String now, String encode) throws UnsupportedEncodingException {
        // Because the data may be encoded by the URL more than once,
        // it needs to be decoded recursively until it is fully successful
        
        if (StringUtils.equals(pre, now.replace("+", "%2B"))) {
            return now;
        }
        pre = now;
        now = URLDecoder.decode(now, encode);
        return innerDecode(pre, now, encode);
    }
}