package com.beta.basic.controller;

import com.alibaba.fastjson.JSON;
import com.beta.basic.domain.ResponseObj;
import com.beta.basic.domain.User;
import com.beta.basic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by yaoyt on 16/12/13.
 *
 * @author yaoyt
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;    //自动载入Service对象
    private ResponseObj responseObj;    //bean对象

    /**
     * 为什么返回值是一个ModelAndView，ModelAndView代表一个web页面<br/>
     * setViewName是设置一个jsp页面的名称
     * @param req   http请求
     * @param user  发起请求后，spring接收到请求，然后封装的bean数据
     * @return  返回一个web页面
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/reg", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public Object reg(HttpServletRequest request, HttpServletResponse response, User user, HttpSession session) throws Exception {
        Object result;
        responseObj = new ResponseObj<User>();
        if (null == user) {
            responseObj.setCode(ResponseObj.FAILED);
            responseObj.setMsg("用户信息不能为空！");
            result = JSON.toJSONString(responseObj);
            return result;
        }
        if (StringUtils.isEmpty(user.getLoginId()) || StringUtils.isEmpty(user.getPwd())) {
            responseObj.setCode(ResponseObj.FAILED);
            responseObj.setMsg("用户名或密码不能为空！");
            result = JSON.toJSONString(responseObj);
            return result;
        }
        if (null != userService.findUser(user)) {
            responseObj.setCode(ResponseObj.FAILED);
            responseObj.setMsg("用户已经存在！");
            result = JSON.toJSONString(responseObj);
            return result;
        }
        try {
            userService.add(user);
        } catch (Exception e) {
            e.printStackTrace();
            responseObj.setCode(ResponseObj.FAILED);
            responseObj.setMsg("其他错误！");
            result = JSON.toJSONString(responseObj);
            return result;
        }
        responseObj.setCode(ResponseObj.OK);
        responseObj.setMsg("注册成功");
        user.setPwd(session.getId());   //单独设置密码为sessionId 误导黑客，前端访问服务器的时候必须有这个信息才能操作
        user.setNextUrl(request.getContextPath() + "/home");    //单独控制地址
        responseObj.setData(user);
        session.setAttribute("userInfo", user); //将一些基本信息写入到session中
        result = JSON.toJSONString(responseObj);
        return result;
    }




    //我们在UserController这个控制器里添加这个方法
    @RequestMapping(value = "/uploadHeadPic"
            , method = RequestMethod.POST
            , produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object uploadHeadPic(@RequestParam(required = false) MultipartFile file, HttpServletRequest request) {
        //在这里面文件存储的方案一般是：收到文件→获取文件名→在本地存储目录建立防重名文件→写入文件→返回成功信息
        //如果上面的步骤中在结束前任意一步失败，那就直接失败了。
        if (null == file || file.isEmpty()) {
            responseObj = new ResponseObj();
            responseObj.setCode(ResponseObj.FAILED);
            responseObj.setMsg("文件不能为空");
            return JSON.toJSONString(responseObj);
        }
        responseObj = new ResponseObj();
        responseObj.setCode(ResponseObj.OK);
        responseObj.setMsg("文件长度为：" + file.getSize());
        return JSON.toJSONString(responseObj);
    }


    //我们在UserController这个控制器里添加这个方法
    @RequestMapping(value = "/userinfo"
            , produces = "application/json; charset=utf-8")
    @ResponseBody
    public String userinfo( HttpServletRequest request) {

        responseObj = new ResponseObj();
        responseObj.setCode(ResponseObj.OK);
        responseObj.setMsg("用户信息123123123");
        return JSON.toJSONString(responseObj);
    }



    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ResponseObj getResponseObj() {
        return responseObj;
    }

    public void setResponseObj(ResponseObj responseObj) {
        this.responseObj = responseObj;
    }
}
