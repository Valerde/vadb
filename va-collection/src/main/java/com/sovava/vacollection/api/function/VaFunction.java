package com.sovava.vacollection.api.function;

@FunctionalInterface
public interface VaFunction <T,R>{
    R apply(T t);
}
