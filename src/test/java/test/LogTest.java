package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yaoyt on 17/3/21.
 *
 * @author yaoyt
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用Spring Junit4进行测试
@ContextConfiguration({"classpath:spring/spring-*.xml"}) //加载配置文件
public class LogTest {
    private static Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void logTC1(){
        logger.error("error");
        logger.debug("debug");
        logger.info("info");
        logger.trace("trace");
        logger.warn("warn");
        logger.error("error {}", "param");
    }

}
