package com.beta.basic.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Created by yaoyt on 17/4/1.
 *
 * @author yaoyt
 */
public class PasswordUtil {

    public static String encryptByPwdAndSalt(String pwd,String salt){
        return new SimpleHash("SHA-1",pwd,salt).toString();
    }
}
