package com.cgzy.utils;

import java.util.Random;

/**
 *       Test
 * 用于生成注册用户的时候生成的salt盐随机数
 */
public class SaltUtils {
    public static String getSalt(int n){
        //定义数组
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()".toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String salt = getSalt(4);
        System.out.println(salt);
    }

}
