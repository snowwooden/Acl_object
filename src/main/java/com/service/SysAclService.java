package com.service;

import com.beanparam.SysAclParam;
import com.beanparam.SysPageBean;
import com.beanparam.SysUserParam;
import com.pojo.SysAcl;
import com.pojo.SysUser;

public interface SysAclService {
    //删除一个权限点
    boolean deleteAcl(SysAclParam param);
    //添加权限点的方法
    boolean saveAcl(SysAclParam param);
    //修改权限点的方法
    boolean updateAcl(SysAclParam param);
    //查询权限点的方法按id
    SysPageBean<SysAcl> showAcl(Integer deptId, SysPageBean<SysAcl> param);
}
