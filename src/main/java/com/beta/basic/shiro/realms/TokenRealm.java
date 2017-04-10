package com.beta.basic.shiro.realms;

import com.beta.basic.domain.Menu;
import com.beta.basic.domain.User;
import com.beta.basic.service.UserService;
import com.beta.basic.shiro.Principal;
import com.beta.basic.shiro.token.StatelessToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaoyt on 17/4/1.
 *
 * @author yaoyt
 */
public class TokenRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    public boolean supports(AuthenticationToken token) {
        //仅支持StatelessToken类型的Token
        return token instanceof StatelessToken;
    }
    /*
	 * 登录信息和用户验证信息验证(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        StatelessToken statelessToken = (StatelessToken) token;
        String username = statelessToken.getUsername();
        String tokenKey = statelessToken.getTokenKey();
        if (null != tokenKey) {
            //校验tokenKey是否在缓存中存在,如果存在,返回
            //模拟一个user用户
            if("Bearer 123!@#".equals(tokenKey)){
                User user = new User();
                //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
                // SimpleAuthenticationInfo 中的 credentials (密码) 选项 将与 token 中的 credentials 进行比较,相同才能校验通过
                SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                        new Principal(user), //用户名
                        tokenKey, //密码
                        null, //将username作为加密盐,通常的做法是用username+随机数作为盐
                        getName()  //realm name
                );
                return authenticationInfo;
            }else{
                return null;
            }
            // return new SimpleAuthenticationInfo(username, password, getName());
        } else {
            return null;
        }
    }

    /*
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Principal principal = (Principal) getAvailablePrincipal(principalCollection);
        User user = userService.findUser(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<Menu> list = new ArrayList<Menu>();
            for (Menu menu : list) {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    // 添加基于Permission的权限信息
                    info.addStringPermission(menu.getPermission());
                }
            }
            return info;
        } else {
            return null;
        }
    }


}
