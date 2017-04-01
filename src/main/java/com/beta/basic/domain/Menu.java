package com.beta.basic.domain;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by yaoyt on 16/12/12.
 *
 * @author yaoyt
 */
public class Menu implements Serializable {

    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
