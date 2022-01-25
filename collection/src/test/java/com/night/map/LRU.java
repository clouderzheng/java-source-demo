package com.night.map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LRU<K,V> extends LinkedHashMap<K,V> {

    public LRU(int initialCapacity,
               float loadFactor,
               boolean accessOrder){
        super(initialCapacity,loadFactor,accessOrder);
    }


    public static void main(String[] args) {
        LRU<Character,Integer> lru = new LRU<Character,Integer>(16,0.75f,true);
        String param = "abcdefghijklmn";
        AtomicInteger i = new AtomicInteger(1);

        for(Character c : param.toCharArray()){
            lru.put(c, i.getAndIncrement());
        }

        for(Map.Entry<Character, Integer> entry: lru.entrySet()){
            System.out.println ( " key :" + entry.getKey() +  " value  "+ entry.getValue() );
        }
        System.out.println(lru);


        System.out.println("d --> " +lru.get('d'));

        for(Map.Entry<Character, Integer> entry: lru.entrySet()){
            System.out.println ( " key :" + entry.getKey() +  " value  "+ entry.getValue() );
        }
        System.out.println(lru);


    }

}


