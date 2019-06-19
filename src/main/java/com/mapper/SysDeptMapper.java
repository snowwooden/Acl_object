package com.mapper;

import com.pojo.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);
    // 查询所有的部门
     List<SysDept> getAllDept();
     //查询是否存在相同部门的方法
    int checkExit(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);
    /*根据父ID查询所有子数据*/
    List<SysDept> selectByParentId(@Param("parentId")Integer parentId);
}