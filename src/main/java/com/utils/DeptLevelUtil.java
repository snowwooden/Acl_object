package com.utils;

public class DeptLevelUtil {
     public  final static String ROOT = "0";
    public  final static String SPRATER = ".";


    public static String getLevel(String parentid, Integer id) {
        String   newlevel = parentid + SPRATER + id;
        return newlevel;
    }
}
