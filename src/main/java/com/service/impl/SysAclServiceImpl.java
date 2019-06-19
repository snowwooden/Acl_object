package com.service.impl;

import com.beanparam.SysAclParam;
import com.beanparam.SysPageBean;
import com.exception.ParamException;
import com.mapper.SysAclMapper;
import com.pojo.SysAcl;
import com.pojo.SysUser;
import com.service.SysAclService;
import com.utils.BeanValidator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysAclServiceImpl implements SysAclService {
    @Resource
    private SysAclMapper mapper;
    /**
     * 删除权限点
     *
     * @param param
     * @return
     */
    @Override
    public boolean deleteAcl(SysAclParam param) {
        int id = param.getId();
        if (id != 0) {
            int i = mapper.deleteByPrimaryKey(id);
            if (i == 0) {
                return false;
            }
            return true;
        }
        throw new ParamException("id异常");
    }
    /**
     * 添加一个权限点
     *
     * @param param
     * @return
     */
    @Override
    public boolean saveAcl(SysAclParam param) {
        BeanValidator.check(param);
        if (param != null) {
            SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).
                    seq(param.getSeq()).remark(param.getRemark()).status(param.getStatus()).
                    url(param.getUrl()).type(param.getType()).build();
            acl.setOperateIp("127.0.1");
            acl.setOperateTime(new Date());
            acl.setOperator("user");
            int i = mapper.insert(acl);
            if (i == 0) {
                return false;
            }
            return true;
        }
        throw new ParamException("参数异常");
    }
    /**
     * 更新一个权限点
     *
     * @param param
     * @return
     */
    @Override
    public boolean updateAcl(SysAclParam param) {
        BeanValidator.check(param);
        if (param != null) {
            SysAcl acl = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).
                    seq(param.getSeq()).remark(param.getRemark()).status(param.getStatus()).
                    url(param.getUrl()).type(param.getType()).build();
            acl.setOperateIp("127.0.1");
            acl.setOperateTime(new Date());
            acl.setOperator("user");
            int i = mapper.updateByPrimaryKeySelective(acl);
            if (i == 0) {
                return false;
            }
            return true;
        }
        throw new ParamException("参数异常");
    }
    /**
     * 展示权限点
     *
     * @param aclModuleId
     * @param param
     * @return
     */
    @Override
    public SysPageBean<SysAcl> showAcl(Integer aclModuleId, SysPageBean<SysAcl> param) {
        //参数校验
        BeanValidator.check(param);
        //判断是否有数据
        int i = mapper.selectAclCount(aclModuleId);
        if (i > 0) {
            SysPageBean<SysAcl> pageBean = new SysPageBean<>();
            List<SysAcl> sysAcls = mapper.selectByaclModuleId(aclModuleId, param);
            pageBean.setData(sysAcls);
            pageBean.setTotal(i);
            return pageBean;
        }
        //查询数据
        return new SysPageBean<>();
    }
}
