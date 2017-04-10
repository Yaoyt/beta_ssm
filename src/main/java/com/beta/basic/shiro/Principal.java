package com.beta.basic.shiro;

import com.beta.basic.domain.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaoyt on 17/4/10.
 *
 * @author yaoyt
 */
public class Principal implements Serializable {
    private static final long serialVersionUID = 1L;


    private String loginName;
    private String name;
    private Map<String, Object> cacheMap;

    public Principal(User user) {
        this.loginName = user.getLoginId();
        this.name = user.getName();
    }

    public String getLoginName() {
        return loginName;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getCacheMap() {
        if (cacheMap == null) {
            cacheMap = new HashMap<String, Object>();
        }
        return cacheMap;
    }

}
