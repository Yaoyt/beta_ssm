package com.beta.basic.service.impl;

import com.beta.basic.dao.UserDao;
import com.beta.basic.domain.User;
import com.beta.basic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by yaoyt on 16/12/12.
 *
 * @author yaoyt
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public void add(User user) throws Exception {
        //先检查用户是否存在
        if (null == user) {
            //抛出用户为空的自定义异常
            throw new Exception("User can not be Null");
        }
        //用户名不能为空检查
        if (StringUtils.isEmpty(user.getLoginId())) {
            //抛出用户名为空的自定义异常
            throw new Exception("User name can not be Null");
        }

        //用户密码不能为空检查
        if (StringUtils.isEmpty(user.getPwd())) {
            //抛出用户密码为空的自定义异常
            throw new Exception("User name can not be Null");
        }

        //由于我这个是仓库管理系统，根据业务需求来说，我们的用户基本信息都是不能为空的
        //基本信息包括：姓名、年龄、用户名、密码、性别、手机号，年龄大于18
        if (StringUtils.isEmpty(user.getSex())
                || user.getAge() > 18
                || StringUtils.isEmpty(user.getCellNumber())) {
            //其他综合异常
            throw new Exception("Some use's base info can not be null");
        }

        //已经存在相同用户
        if (null != userDao.findOneById(user.getLoginId())) {
            //存在相同的用户异常
            throw new Exception("Register User Failed，Because the  user Aiready exist");
        }
        int result = 0; //受影响的行数默认为0
        try {
            result = userDao.add(user);
        } catch (Exception e) {
            System.out.println("添加用户失败,用户已经存在");
            //其他用户添加失败异常
            throw new Exception(e);
        }
        if (result > 0)
            System.out.println("添加用户成功");
    }

    public User findUser(User user) throws Exception {
        return userDao.findOneById(user.getLoginId());
    }

    @Override
    public User findUser(String username, String password) {
        return userDao.findOneById(username);
    }

    @Override
    public User findUser(String username) {
        return userDao.findOneById(username);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


}
