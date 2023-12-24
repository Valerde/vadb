package com.sovava.vacollection.api;

public interface VaComparable <T> extends Comparable<T>{

    @Override
    int compareTo(T o);
}
