package com.night.map;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashmapTest {


    public static void main(String[] args) {

        ConcurrentHashMap<String,String> map = new ConcurrentHashMap();

        map.put("name","night");
        map.get("name");

        System.out.println(map);


    }
}
