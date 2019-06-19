package com.service;

import com.beanparam.SysRoleParam;
import com.pojo.SysRole;
import com.pojo.SysUser;

import java.util.List;

public interface SysRoleService {
    boolean saveRole(SysRoleParam param);
    boolean updateRole(SysRoleParam param);
    List<SysRole> showRoleList();
    void updateAclRoleTree(Integer roleId, List<Integer> aclIdList);
    List<SysUser> showRoleUsers(Integer roleId);
}
