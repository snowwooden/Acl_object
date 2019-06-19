package com.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *  自动生成Tree的工具类
 *
 *  (仅适用于这次的项目)
 *
 *  使用方法：
 *  1.将数据库返回类型设置为 resultType="java.util.Map"
 *  2.dao层与之对应返回类型设置为  List<Map>
 *  3.调用该方法中的Tree方法，第一个参数List为数据库查询出来的数据，第二个参数为需要设置的下层数据的数组名称(如：aclModuleList、deptList等)
 *  4.将Tree方法返回的值放入JsonData中即可
 *
 *  本工具类原理与老师所讲的大致相同，只是将dto等类换成了List、Map等更加通用的集合，使得通用性更高，写出来的目的是为了简化代码，提高开发效率
 *
 *  By：凯凯
 **/
public class TreeUtils {

    public static List<Map> Tree(List<Map> all,String key){

        List<Map> firstList = new ArrayList<>();
        Multimap<String,Map> multiMap = ArrayListMultimap.create();
        for (Map m : all) {

            m.put(key,new ArrayList<>());
            //LevelUtils.ROOT其实就是0，如果你没有LevelUtils类或者类的名字不同可以改名或者将LevelUtils.ROOT改为0
            if (DeptLevelUtil.ROOT.equals(m.get("level"))){
                firstList.add(m);
            }

            multiMap.put((String) m.get("level"),m);

        }
        //根据seq排序
        Collections.sort(firstList,ListSortUtils.deptSeqComparator);
        //调用生成下层数据的方法
        recursionDeptTree(firstList,multiMap,key);
        return firstList;
    }


    public static void recursionDeptTree(List<Map> firstList, Multimap<String,Map> multiMap,String key){

        for (int i=0;i<firstList.size();i++){

            Map map = firstList.get(i);

            String nextLevel = DeptLevelUtil.getLevel((String) map.get("level"), (Integer) map.get("id"));

            List<Map> nextList = (List<Map>) multiMap.get(nextLevel);

            if (nextList!=null){

                Collections.sort(firstList,ListSortUtils.deptSeqComparator);

                map.remove(key);
                map.put(key,nextList);
                TreeUtils.recursionDeptTree(nextList,multiMap,key);

            }

        }

    }


}
