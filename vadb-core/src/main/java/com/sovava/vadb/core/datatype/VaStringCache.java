package com.sovava.vadb.core.datatype;

import com.sovava.vadb.core.api.datatypeApi.VaCacheI;
import com.sovava.vadb.core.api.datatypeApi.VaStringCacheI;

/**
 * Description:
 *
 * @author: ykn
 * @date: 2023年12月21日 7:41 PM
 **/
public class VaStringCache<V> implements VaStringCacheI<V> {
    @Override
    public VaCacheI<String, V> set(String key, V value) {
        return null;
    }

    @Override
    public VaCacheI<String, V> setUntil(String key, V value, long milliseconds) {
        return null;
    }

    @Override
    public V get(String key) {
        return null;
    }

    @Override
    public VaCacheI<String, V> expireUntilMilSec(String key, long milliseconds) {
        return null;
    }
}
