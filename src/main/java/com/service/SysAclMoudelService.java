package com.service;

import com.beanparam.SysAclMoudelParam;

import java.util.List;
import java.util.Map;

public interface SysAclMoudelService {
    //添加一个权限模型
    boolean saveAclMoudel(SysAclMoudelParam param);
    //更新一个权限模型
    boolean updateAclMoudel(SysAclMoudelParam param);
    //删除一个权限模块
    boolean deleteAclMoudel(Integer id);
    //展示权限模块树
    List<Map> showAclMoudelTree();
}
