package com.service;

import com.beanparam.SysDeptUserParam;
import com.beanparam.SysPageBean;
import com.beanparam.SysUserLoginParam;
import com.beanparam.SysUserParam;
import com.pojo.SysRoleUser;
import com.pojo.SysUser;

import java.util.List;

public interface SysUserService {
    //用户登录
    SysUser login(SysUserLoginParam param);
    //添加用户的方法
    boolean savaUser(SysUserParam param);
    //修改用户的方法
    boolean updateUser(SysUserParam param);
    //查询用户的方法按id
    SysPageBean<SysUser> showUsers(Integer deptId, SysPageBean<SysUser> param);

}
