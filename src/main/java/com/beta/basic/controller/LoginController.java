package com.beta.basic.controller;

import com.alibaba.fastjson.JSON;
import com.beta.basic.domain.ResponseObj;
import com.beta.basic.domain.User;
import com.beta.basic.service.UserService;
import com.beta.basic.utils.PasswordUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by yaoyt on 17/3/21.
 *
 * @author yaoyt
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserService userService;    //自动载入Service对象
    private ResponseObj responseObj;    //bean对象

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value = "/initLogin",method = RequestMethod.GET)
    public String login(){

        return "login";
    }

    /**
     * 登录接口，因为json数据外层一般都是Object类型，所以返回值必须是Object<br/>
     *  这里的地址是： 域名/userAction/login
     *
     * @param
     * @param user
     * @return
     */
    @RequestMapping(value = "/login"    //内层地址
            , method = RequestMethod.POST   //限定请求方式
            , produces = "application/json; charset=utf-8") //设置返回值是json数据类型
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response, User user, HttpSession session) throws Exception {
        Object result;
        if (null == user) {
            responseObj = new ResponseObj<User>();
            responseObj.setCode(ResponseObj.EMPUTY);
            responseObj.setMsg("登录信息不能为空");
            result = JSON.toJSONString(responseObj);   //通过gson把java bean转换为json
            return result; //返回json
        }
        if (StringUtils.isEmpty(user.getLoginId()) || StringUtils.isEmpty(user.getPwd())) {
            responseObj = new ResponseObj<User>();
            responseObj.setCode(ResponseObj.FAILED);
            responseObj.setMsg("用户名或密码不能为空");
            result = JSON.toJSONString(responseObj);
            return result;
        }
        //查找用户
        User user1 = userService.findUser(user);
        if (null == user1) {
            responseObj = new ResponseObj<User>();
            responseObj.setCode(ResponseObj.EMPUTY);
            responseObj.setMsg("未找到该用户");
            result = JSON.toJSONString(responseObj);
        } else {
            String password = PasswordUtil.encryptByPwdAndSalt(user.getPwd(),user.getLoginId());  // 将密码进行加密,放入到token中,与realm中从数据库中查询的密码进行比较.
            // shiro加入身份验证
            UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginId(), password);
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(token);
                user1.setNextUrl(request.getContextPath() + "/home");
                responseObj = new ResponseObj<User>();
                responseObj.setCode(ResponseObj.OK);    //登录成功，状态为1
                responseObj.setMsg(ResponseObj.OK_STR);
                responseObj.setData(user1); //登陆成功后返回用户信息
            } catch (AuthenticationException e) {
                responseObj = new ResponseObj<User>();
                responseObj.setCode(ResponseObj.FAILED);
                responseObj.setMsg("用户密码错误");
            }
            result = JSON.toJSONString(responseObj);
        }
        return result;
    }

    /**
     * 后台主页
     *
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    /**
     * 后台主页
     *
     * @return
     */
    @RequestMapping(value = "/authorized")
    public String authorized() {
        return "authorized";
    }

}
