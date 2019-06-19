package com.mapper;

import com.pojo.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);
    /*查询所有记录*/
    List<Map> selectAll();
    /*根据父id查询所有*/
    List<SysAclModule> selectByParentId(@Param("parentId")Integer parentId);

    List<SysAclModule> findAclModel();//todo 生成
}