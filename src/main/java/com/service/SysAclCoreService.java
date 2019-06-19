package com.service;

import com.google.common.collect.Lists;
import com.mapper.SysAclMapper;
import com.mapper.SysRoleAclMapper;
import com.mapper.SysRoleUserMapper;
import com.mapper.SysUserMapper;
import com.pojo.SysAcl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysAclCoreService {
    //注入mapper
    @Resource
    private SysAclMapper aclMapper;
    @Resource
    private SysRoleAclMapper roleAclMapper;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysRoleUserMapper roleUserMapper;

    //更据已经登录的用户的id去获得改用户可支配的所有权限点对象
    public List<SysAcl> getAclsByUserId() {
        //获取当前登录对像的id
        Integer id = 1;/*RequsetHolder.getUser().getId();*/
        System.out.println(id);
        if (id != null) {
            //判断是否为超管 ，超管取得所有权限点
            if (isSuperAdmin(id)) {
                return aclMapper.selecAllAcl();
            }
            //更具用户id查询用户拥有的角色id
            List<Integer> roleIds = roleUserMapper.selRoleIdsByUserId(id);
            if (CollectionUtils.isEmpty(roleIds)) {
                return Lists.newArrayList();
            }
            //根据角色id查询所有权限点id
            List<Integer> aclIds = roleAclMapper.selAclIdsByRoleId(roleIds);
            if (CollectionUtils.isEmpty(aclIds)) {
                return Lists.newArrayList();
            }
            //根据权限点id查询权限点对象
            List<SysAcl> sysAcls = aclMapper.selectByAclId(aclIds);
            if (CollectionUtils.isEmpty(sysAcls)) {
                return Lists.newArrayList();
            }
            //封装返回
            return sysAcls;
        }
        return Lists.newArrayList();
    }
    //根据页面传过来的角色id查出改角色所拥有的全部权限点对象
    public  List<SysAcl> getAclByRoleId(int roleId){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(roleId);
        List<Integer> aclIds = roleAclMapper.selAclIdsByRoleId(list);
        if (CollectionUtils.isEmpty(aclIds)){
            return Lists.newArrayList();
        }
        List<SysAcl> sysAcls = aclMapper.selectByAclId(aclIds);
        if (CollectionUtils.isEmpty(sysAcls)){
            return Lists.newArrayList();
        }

        return sysAcls;
    }


    //判断是否是超级用户
    public boolean isSuperAdmin(int id) {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        if (userMapper.selectByPrimaryKey(id).getUsername().contains("Admin")) {
            return true;
        }
        return false;
    }
    /**
     * 根据请求路径判断是否具有相关权限
     * @param url
     * @return
     */
    /*public boolean hasUrlAcl(String url) {
        //判断是否是超级管理员
        if (isSuperAdmin()) {
            return true;
        }
        //根据当前用户访问的url获取到权限对象
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        //url不为空的情况下，就需要查询当前用户拥有的所有权限点 直接调用getUserAclList
        List<SysAcl> userAclList = getUserAclList();
        //如果aclList是集合，最好遍历 不是就不需要遍历 直接判断
        for (SysAcl acl : aclList) {
            // 判断一个用户是否具有某个权限点的访问权限
            if (userAclList.contains(acl)) {
                return true;
            }
        }
        return false;
    }*/
}
