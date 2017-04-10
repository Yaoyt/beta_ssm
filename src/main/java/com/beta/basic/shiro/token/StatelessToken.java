package com.beta.basic.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by yaoyt on 17/4/7.
 *
 * @author yaoyt
 */
public class StatelessToken implements AuthenticationToken {

    private String username;

    private String tokenKey;

    public StatelessToken(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return tokenKey;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
