package com.beta.basic.shiro;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.web.mgt.CookieRememberMeManager;

/**
 * Created by yaoyt on 17/4/7.
 *
 * @author yaoyt
 */
public class CookieRememberMeManagerForBeta extends CookieRememberMeManager {
    public CookieRememberMeManagerForBeta(){
        super();
        String cipherStr = "4AvVhmFLUs0KTA3Kprsdag==";
        byte[] cipherKey = Base64.decode(cipherStr);
        setCipherKey(cipherKey);
    }
}
