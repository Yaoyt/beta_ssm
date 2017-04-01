package com.beta.basic.service;


import com.beta.basic.domain.User;

/**
 * Created by yaoyt on 16/12/12.
 *
 * @author yaoyt
 */
public interface UserService extends BaseService<User> {
    //添加用户的实例
    public void add(User user) throws Exception;

    //查询用户
    public User findUser(User user) throws Exception;

    public User findUser(String username, String password);

    public User findUser(String username);
}
