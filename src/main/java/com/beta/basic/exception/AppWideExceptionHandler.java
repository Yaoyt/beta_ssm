package com.beta.basic.exception;

import com.alibaba.fastjson.JSON;
import com.beta.basic.domain.ResponseObj;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yaoyt on 17/4/11.
 * 异常处理公共类,可以捕捉到整个应用中每个方法上声明的对应异常
 * 并调用对应的方法逻辑进行处理.
 * @author yaoyt
 */
@ControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(NumberFormatException.class)
    @RequestMapping(produces = "application/json; charset=utf-8")
    @ResponseBody
    public String nullPoninterHandler(){
        ResponseObj responseObj = new ResponseObj();
        responseObj.setCode(ResponseObj.FAILED);
        responseObj.setMsg("内部类型转换异常");
        return JSON.toJSONString(responseObj);
    }
}
