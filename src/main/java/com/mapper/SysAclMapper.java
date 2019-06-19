package com.mapper;

import com.beanparam.SysPageBean;
import com.pojo.SysAcl;
import com.pojo.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);
    /**/
    List<SysAcl> selectByaclModuleId(@Param("aclModuleId") Integer aclModuleId, @Param("param") SysPageBean<SysAcl> pageBean);
    /*查询一共多少条员工记录*/
    int selectAclCount(@Param("aclModuleId") Integer aclModuleId);

    List<SysAcl> selecAllAcl();

    List<SysAcl> selectByAclId(@Param("ids") List<Integer> aclIds);
}