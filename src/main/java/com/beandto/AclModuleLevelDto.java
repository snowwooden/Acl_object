package com.beandto;

import com.google.common.collect.Lists;

import com.pojo.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AclModuleLevelDto extends SysAclModule {
    //存储下层权限模块
    List<AclModuleLevelDto> aclModuleList = new ArrayList<>();

    //封装当前权限模块下的权限点
    List<SysAclDto> aclList = new ArrayList<>();

    public static AclModuleLevelDto adapt(SysAclModule aclModule) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }
}
