package com.sovava.vadb.test.collection.utils;

import java.util.Random;

/**
 * Description: 生成随机对象
 *
 * @author: ykn
 * @date: 2023年12月24日 7:59 PM
 **/
public class RandomObject {
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
