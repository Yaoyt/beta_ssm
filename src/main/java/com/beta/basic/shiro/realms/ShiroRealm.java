package com.beta.basic.shiro.realms;

import com.beta.basic.domain.User;
import com.beta.basic.service.UserService;
import com.beta.basic.shiro.Principal;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaoyt on 17/4/1.
 *
 * @author yaoyt
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /*
	 * 登录信息和用户验证信息验证(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
        String username = (String) usernamePasswordToken.getPrincipal(); // 得到用户名
        String password = new String((char[]) usernamePasswordToken.getCredentials()); // 得到密码

        if (null != username && null != password) {
            User user =  userService.findUser(username,password);
            //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    new Principal(user), //用户名
                    user.getPwd(), //密码
                    ByteSource.Util.bytes(user.getLoginId()),//将username作为加密盐,通常的做法是用username+随机数作为盐
                    getName()  //realm name
            );
            return authenticationInfo;
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
        if(null == principalCollection){
            return null;
        }
        AuthorizationInfo info = null;
        Cache<Object,AuthorizationInfo> cache = getAuthorizationCache();
        if(cache != null){
            Object key = getAuthenticationCacheKey(principalCollection);
            info = cache.get(key);
        }
        if(null == info){
            info = doGetAuthorizationInfo(principalCollection);
            if(null != info && null != cache){
                Object key = getAuthenticationCacheKey(principalCollection);
                cache.put(key,info);
            }
        }
        return info;

        /*Principal principal = (Principal) getAvailablePrincipal(principalCollection);
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
        }*/
    }

}
