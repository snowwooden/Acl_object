package com.mapper;


import com.beanparam.SysPageBean;
import com.beanparam.SysUserLoginParam;
import com.pojo.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
    /**/
    List<SysUser> selectByDeptId(@Param("deptId") Integer deptId, @Param("param")SysPageBean<SysUser> pageBean);
    /*查询一共多少条员工记录*/
    int selectDeptCount(@Param("deptId") Integer deptId);
    SysUser selectByUsername(@Param("username") String username);

    /*根据id列表查询*/
    List<SysUser> selectByUserIds(@Param("userIds") List<Integer> userids);
    /*查询全部*/
    List<SysUser> selectAll();
}