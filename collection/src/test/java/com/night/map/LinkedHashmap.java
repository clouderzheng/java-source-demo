package com.night.map;

public class LinkedHashmap {


    public static void main(String[] args) {

        java.util.LinkedHashMap<String,String> linkedHashmap = new java.util.LinkedHashMap<>();
        linkedHashmap.put("name","tom");
        linkedHashmap.put("age","28");
        linkedHashmap.put("addr","成都");

        linkedHashmap.forEach( (x,y) -> System.out.println( " key : "+ x+"  value :" + y));


    }
}
