package com.mapper;

import com.pojo.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    /*更据角色id查询改用户可支配的所有权限点id*/
    List<Integer> selAclIdsByRoleId(@Param("roleId") List<Integer> roleIds);

    /*按roleid删除记录*/
    int  deleteByRoleId(@Param("roleId") Integer roleId);
    /*插入*/
    void batchInsert(@Param("roleAclList") List<SysRoleAcl> roleAclList);



}