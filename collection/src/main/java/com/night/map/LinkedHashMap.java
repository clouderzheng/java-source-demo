package com.night.map;

public class LinkedHashMap<K,V> {

    static class Entry<K,V> extends HashMap.Node<K,V>{
        Entry<K,V> before,after;
        Entry(int hash, K key, V value, HashMap.Node next){
            super(hash,key,value,next);
        }

    }
}
