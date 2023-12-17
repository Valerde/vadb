package com.sovava.vadb.test;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2023年12月17日 11:38 PM
 **/
@Slf4j
public class test {
    @Test
    public void test(){

        Object parse = JSON.parse("{\"name\":\"ykn\",\"age\":3}");
        log.warn(parse.toString());
        log.error("test junit");
    }
}
