package test;

import org.apache.shiro.codec.Base64;
import org.junit.Test;

import java.security.MessageDigest;

/**
 * Created by yaoyt on 17/4/7.
 *
 * @author yaoyt
 */
public class Base64Test {

    @Test
    public void test1() throws Exception {
        String data = "abcd";
        byte[] bytesOfMessage = "123123".getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] b = md.digest(bytesOfMessage); //Returns the SHA 256 hash and converts it into byte
        // Continue with your code
        System.out.println(b.length);
        byte[] b2 = Base64.decode("4AvVhmFLUs0KTA3Kprsdag==");

        System.out.println(b2.length);
    }
}
