package com.beta.basic.dao;


import com.beta.basic.domain.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaoyt on 16/12/12.
 *
 * @author yaoyt
 */
public interface UserDao extends Dao<User> {

    int add(User user);

    int del(User user);

    int update(User user);

    User findOneById(Serializable Id);

    List<User> findAll();

}
