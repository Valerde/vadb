package com.sovava.vacollection.exception;

/**
 * Description: 表示没有这个元素
 *
 * @author: ykn
 * @date: 2023年12月20日 5:27 PM
 **/
public class VaNoSuchElementException extends RuntimeException{
    public VaNoSuchElementException() {
        super();
    }

    public VaNoSuchElementException(String message) {
        super(message);
    }
}
