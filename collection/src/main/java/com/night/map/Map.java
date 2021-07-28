package com.night.map;

public interface Map<K,V> {

    int size();

    V get(K key);

    V put(K key, V value);

    V remove(Object key);

    public interface Entry<K,V> {

        K getKey();

        V getValue();


    }

}
