package com.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mapper.SysRoleUserMapper;
import com.mapper.SysUserMapper;
import com.pojo.SysRoleUser;
import com.pojo.SysUser;
import com.utils.IpUtils;
import com.utils.RequestHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleUserService sysRoleUserService;


    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.selectUserIdByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.selectByUserIds(userIdList);
    }

    //将当前角色已拥有的和未拥有的用户列表封装到map中
    public Map getUserMapByRoleId(int roleId){
        //已选择的用户
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        //全部用户
        List<SysUser> allUserList = sysUserMapper.selectAll();
        //未选择的用户
        List<SysUser> unselectedUserList = Lists.newArrayList();
        for(SysUser sysUser : allUserList) {
            //判断用户的状态并且已选择的用户中不包含这个id
            if (sysUser.getStatus() == 1 && !selectedUserList.contains(sysUser)) {
                unselectedUserList.add(sysUser);
            }
        }
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);
        return map;
    }

    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.selectUserIdByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
    }

    @Transactional
    public void updateRoleUsers(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().
                    roleId(roleId).userId(userId).
                    operator(RequestHolder.getUser().getUsername()).
                    operateIp(IpUtils.getIPAddress(RequestHolder.getRequest())).
                    operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }

}
