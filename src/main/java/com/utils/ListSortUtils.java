package com.utils;



import java.util.Comparator;
import java.util.Map;

/**
 * 排序工具类
 **/
public class ListSortUtils {

    public static Comparator<Map> deptSeqComparator = new Comparator<Map>() {
        @Override
        public int compare(Map o1, Map o2) {
            return (Integer)o1.get("seq")-(Integer)o2.get("seq");
        }
    };

}
