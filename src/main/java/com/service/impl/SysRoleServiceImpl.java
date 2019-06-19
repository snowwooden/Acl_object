package com.service.impl;

import com.beanparam.SysRoleParam;
import com.exception.ParamException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mapper.SysRoleAclMapper;
import com.mapper.SysRoleMapper;
import com.mapper.SysRoleUserMapper;
import com.mapper.SysUserMapper;
import com.pojo.SysRole;
import com.pojo.SysRoleAcl;
import com.pojo.SysUser;
import com.service.SysRoleService;
import com.utils.BeanValidator;
import com.utils.IpUtils;
import com.utils.RequestHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleMapper mapper;
    @Resource
    private SysRoleAclMapper roleAclMapper;
    @Resource
    private SysRoleUserMapper userMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    /**
     * 添加一个角色
     * @param param
     * @return
     */
    @Override
    public boolean saveRole(SysRoleParam param) {
        BeanValidator.check(param);
        if (param!=null){
            SysRole role = SysRole.builder().name(param.getName()).remark(param.getRemark()).
                    status(param.getStatus()).type(param.getType()).build();
            role.setOperateIp(IpUtils.getIPAddress(RequestHolder.getRequest()));
            role.setOperateTime(new Date());
            role.setOperator(RequestHolder.getUser().getUsername());
            int i = mapper.insert(role);
            if (i==0){
                return false;
            }
            return true;
        }
        throw  new ParamException("参数异常");
    }

    /**
     * 修改一个角色
     * @param param
     * @return
     */
    @Override
    public boolean updateRole(SysRoleParam param) {
        BeanValidator.check(param);
        if (param!=null){
            SysRole role = SysRole.builder().id(param.getId()).name(param.getName()).remark(param.getRemark()).
                    status(param.getStatus()).type(param.getType()).build();
            int i = mapper.updateByPrimaryKeySelective(role);
            if (i==0){
                return false;
            }
            return true;
        }
        throw  new ParamException("参数异常");
    }

    /**
     * 展示角色列表
     * @return
     */
    @Override
    public List<SysRole> showRoleList() {
        List<SysRole> roles = mapper.selectAll();
        if (roles!=null){
            return roles;
        }
        return null;
    }

    /**
     * 更新角色权限树
     * @param roleId
     * @param aclIdList
     */
    @Override
    public void updateAclRoleTree(Integer roleId, List<Integer> aclIdList) {
        //获取更新之前分配的权限点集合
        //旧的权限点
        List<Integer> originAclIdList = roleAclMapper.selAclIdsByRoleId(Lists.newArrayList(roleId));
        //判断前后的长度是否一致
        if (originAclIdList.size() == aclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            //在旧的里面删除现在选的
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }
        updateRoleAcls(roleId, aclIdList);
    }


    @Transactional
    public void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        roleAclMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for(Integer aclId : aclIdList) {

            SysRoleAcl roleAcl = SysRoleAcl.builder().
                    roleId(roleId).
                    aclId(aclId).
                    operator(RequestHolder.getUser().getUsername()).
                    operateIp(IpUtils.getIPAddress(RequestHolder.getRequest())).
                    operateTime(new Date()).build();
            roleAclList.add(roleAcl);
        }
        roleAclMapper.batchInsert(roleAclList);
    }


    /**
     * 角色与用户
     * @param roleId
     * @return
     */
    @Override
    public List<SysUser> showRoleUsers(Integer roleId) {
        if (roleId!=null){
            List<Integer> userIds = userMapper.selectUserIdByRoleId(roleId);
            List<SysUser> sysUsers = sysUserMapper.selectByUserIds(userIds);
            return sysUsers;
        }
        return Lists.newArrayList();
    }
}
