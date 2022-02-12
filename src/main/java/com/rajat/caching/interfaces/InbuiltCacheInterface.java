package com.rajat.caching.interfaces;

public interface InbuiltCacheInterface<T> {

    public T getValue(int id);
    public void putValue(int id , T value);

}
