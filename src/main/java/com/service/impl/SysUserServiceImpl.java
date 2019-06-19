package com.service.impl;

import com.beanparam.SysDeptUserParam;
import com.beanparam.SysPageBean;
import com.beanparam.SysUserLoginParam;
import com.beanparam.SysUserParam;
import com.exception.ParamException;
import com.mapper.SysRoleUserMapper;
import com.mapper.SysUserMapper;
import com.pojo.SysRoleUser;
import com.pojo.SysUser;
import com.service.SysUserService;
import com.utils.BeanValidator;
import com.utils.JsonData;
import com.utils.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserMapper mapper;

    /**
     * 用户登录
     *
     * @return
     */
    @Override
    public SysUser login(SysUserLoginParam param) {
        BeanValidator.check(param);
        if (param != null) {
            String username = param.getUsername();
            String password = param.getPassword();
            SysUser sysUser = mapper.selectByUsername(username);
            String pwd = MD5Utils.getPwd(password);
            String errorMsg = "";
            if (sysUser == null) {
                errorMsg = "用户不存在";
            } else if (!sysUser.getPassword().equals(pwd)) {
                errorMsg = "密码不正确";
            } else if (sysUser.getStatus() != 1) {
                errorMsg = "用户状态异常";
            } else {
                return sysUser;
            }
            throw new ParamException(errorMsg);
        }
        throw new ParamException("登录参数异常");
    }

    /**
     * 添加 用户的 方法
     *
     * @param param
     * @return
     */
    @Override
    public boolean savaUser(SysUserParam param) {
        BeanValidator.check(param);
        if (param != null) {
            SysUser sysUser = SysUser.builder().deptId(param.getDeptId()).mail(param.getMail()).
                    username(param.getUsername()).telephone(param.getTelephone()).status(param.getStatus()).
                    remark(param.getRemark()).build();
            sysUser.setOperateIp("0.00");
            sysUser.setOperateTime(new Date());
            sysUser.setOperator("user");
            int i = mapper.insertSelective(sysUser);
            if (i != 0) {
                return true;
            }
            return false;
        }
        throw new ParamException("参数为空");
    }

    /**
     * 更新一个用户
     *
     * @param param
     * @return
     */
    @Override
    public boolean updateUser(SysUserParam param) {
        BeanValidator.check(param);
        if (param != null) {
            SysUser sysUser = SysUser.builder().deptId(param.getDeptId()).mail(param.getMail()).
                    username(param.getUsername()).telephone(param.getTelephone()).status(param.getStatus()).remark(param.getRemark()).id(param.getId()).build();
            //补全参数
            sysUser.setOperator("user");
            sysUser.setOperateTime(new Date());
            sysUser.setOperateIp("null");
            //开始更新
            int i = mapper.updateByPrimaryKeySelective(sysUser);
            if (i == 0) {
                return false;
            }
            return true;
        }
        throw new ParamException("参数错误");
    }

    @Override
    public SysPageBean<SysUser> showUsers(Integer deptId, SysPageBean<SysUser> param) {
        //参数校验
        BeanValidator.check(param);
        //判断是否有数据
        int i = mapper.selectDeptCount(deptId);
        if (i > 0) {
            SysPageBean<SysUser> pageBean = new SysPageBean<>();
            List<SysUser> sysUsers = mapper.selectByDeptId(deptId, param);
            pageBean.setData(sysUsers);
            pageBean.setTotal(i);
            return pageBean;

        }
        //查询数据

        return new SysPageBean<>();
    }


}
