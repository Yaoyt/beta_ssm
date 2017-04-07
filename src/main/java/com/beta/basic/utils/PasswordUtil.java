package com.beta.basic.utils;

import com.beta.basic.domain.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by yaoyt on 17/4/1.
 *
 * @author yaoyt
 */
public class PasswordUtil {

    private static RandomNumberGenerator randomNumberGenerator =  new SecureRandomNumberGenerator();

    private String algorithmName = "md5";

    private final int hashIterations = 2;

    public User encryptPassword(User user) {
        String salt = randomNumberGenerator.nextBytes().toHex();

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPwd(),
                ByteSource.Util.bytes(salt),
                hashIterations).toHex();

        user.setSalt(salt);
        user.setPwd(newPassword);
        return user;
    }


    public static String encryptByPwdAndSalt(String pwd,String salt){

        return new SimpleHash("SHA-1",pwd,salt).toString();
    }
}
