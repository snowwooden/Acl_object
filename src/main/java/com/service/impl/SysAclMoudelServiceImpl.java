package com.service.impl;

import com.beanparam.SysAclMoudelParam;
import com.exception.ParamException;
import com.mapper.SysAclModuleMapper;
import com.pojo.SysAclModule;
import com.pojo.SysDept;
import com.service.SysAclMoudelService;
import com.utils.BeanValidator;
import com.utils.DeptLevelUtil;
import com.utils.TreeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SysAclMoudelServiceImpl implements SysAclMoudelService {
    @Resource
    private SysAclModuleMapper moduleMapper;

    /**
     * 插入权限
     *
     * @param param
     * @return
     */
    @Override
    public boolean saveAclMoudel(SysAclMoudelParam param) {
        BeanValidator.check(param);
        if (param != null) {
            //判断是否已经存在当前的权限
             /*if (checkExit(param.getParentId(), param.getName(), param.getId())>0){
            throw  new ParamException("警告：重复添加部门");
        }*/
            SysAclModule module = SysAclModule.builder().name(param.getName()).parent_id(param.getParentId()).
                    seq(param.getSeq()).remark(param.getRemark()).status(param.getStatus()).build();
            if (param.getParentId() == 0) {
                module.setLevel(DeptLevelUtil.ROOT);
            } else {
                String s = makevel(param.getParentId());
                module.setLevel(DeptLevelUtil.getLevel(s, param.getParentId()));
            }
            module.setOperateIp("127.0.1");
            module.setOperateTime(new Date());
            module.setOperator("user");
            int i = moduleMapper.insertSelective(module);
            if (i == 0) {
                return false;
            }
            return true;
        }
        throw new ParamException("参数错误");
    }

    /**
     * 修改某权限
     *
     * @param param
     * @return
     */
    @Override
    public boolean updateAclMoudel(SysAclMoudelParam param) {
        //参数校验
        BeanValidator.check(param);//todo

        SysAclModule olddept = moduleMapper.selectByPrimaryKey(param.getId());
        if (olddept == null) {
            throw new ParamException("修改的部门不存在");
        }
        SysAclModule newDept = SysAclModule.builder().name(param.getName()).parent_id(param.getParentId()).
                seq(param.getSeq()).status(param.getStatus()).remark(param.getRemark()).build();

        String mylevel = null;
        if (param.getParentId() == 0) {
            mylevel = "0";
        } else {
            mylevel = DeptLevelUtil.getLevel(makevel(param.getParentId()), param.getParentId());
        }
        newDept.setLevel(mylevel);
        return updateChildDept(olddept, newDept);
    }

    /**
     * 删除某个权限
     *
     * @param
     * @return
     */
    @Override
    public boolean deleteAclMoudel(Integer id) {
        if (!moduleMapper.selectByParentId(id).isEmpty()) {
            throw new ParamException("不能越级删除");
        } else {
            int i = moduleMapper.deleteByPrimaryKey(id);
            if (i != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Map> showAclMoudelTree() {
        List<Map> maps = moduleMapper.selectAll();
        List<Map> list = TreeUtils.Tree(maps, "aclModuleList");
        return list;
    }

    /**
     * 修改树的事务
     */
    @Transactional
    public boolean updateChildDept(SysAclModule oldDept, SysAclModule newDept) {
        String newLevel = newDept.getLevel();
        if (!oldDept.getLevel().equals(newLevel)) {
            List<SysAclModule> childDepts = moduleMapper.selectByParentId(oldDept.getId());
            if (!childDepts.isEmpty()) {
                for (SysAclModule child : childDepts) {
                    SysAclModule sysDept = new SysAclModule();
                    BeanUtils.copyProperties(child, sysDept);
                    String level = DeptLevelUtil.getLevel(newLevel, newDept.getId());
                    child.setLevel(level);
                    //递归
                    updateChildDept(sysDept, child);
                }
            }
        }
        int i = moduleMapper.updateByPrimaryKeySelective(newDept);
        if (i != 0) {
            return true;
        }
        return false;
    }

    /*
         赋值level
         */
    public String makevel(Integer deptId) {
        SysAclModule sysDept = moduleMapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return DeptLevelUtil.ROOT;
        }
        return sysDept.getLevel();
    }
}
