package com.nuaa.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String s){
        return DigestUtils.md5Hex(s);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFromPass(String inputPass){
        String str = salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String fromPassToDBPass(String fromPass, String salt) {
        String str = salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    public static String inputPassToDBPass(String inputPass, String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass, salt);
        return dbPass;

    }

    public static void main(String[] args) {
        String s = "123456";
        String fromPass = inputPassToFromPass(s);
        String dbPass = fromPassToDBPass(fromPass, s);
        System.out.println(dbPass);

        String dbPass2 = inputPassToDBPass(s, s);
        System.out.println(dbPass2);
    }


}
