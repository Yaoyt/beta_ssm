package com.beta.basic.sys;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by yaoyt on 17/4/11.
 * 监听ContextRefreshedEvent事件,在spring 启动后执行,可获取到spring中声明的bean
 * @author yaoyt
 */
@Component
public class ApplicationInitializingBean implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){//root application context 没有parent，他就是老大.
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法.
            System.out.println("spring 初始化之后执行的方法. ");

        }
    }
}
