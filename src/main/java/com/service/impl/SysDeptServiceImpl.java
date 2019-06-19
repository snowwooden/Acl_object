package com.service.impl;

import com.beanparam.SysDeptParam;
import com.exception.ParamException;
import com.mapper.SysDeptMapper;
import com.pojo.SysDept;
import com.service.SysDeptService;
import com.utils.BeanValidator;
import com.utils.DeptLevelUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysDeptServiceImpl implements SysDeptService {
    //注入mapper
    @Resource
    private SysDeptMapper mapper;

    @Override
    public boolean saveDept(SysDeptParam param) {
        //校验传进来的数据
        BeanValidator.check(param);
        //跟据id去查询这个数据看是否有存在的
        /*if (checkExit(param.getParentId(), param.getName(), param.getId())>0){
            throw  new ParamException("警告：重复添加部门");
        }*/
        //不存在就加进去存在就返异常
        //给药添加的参数值
        SysDept sysDept = SysDept.builder().name(param.getName()).parentId(param.getParentId()).
                seq(param.getSeq()).remark(param.getRemark()).build();
        //剩余参数自己给值
        if (param.getParentId()==0){
            sysDept.setLevel(DeptLevelUtil.ROOT);
        }else {
            sysDept.setLevel(DeptLevelUtil.getLevel(makevel(param.getParentId()), param.getParentId()));
        }
        sysDept.setOperateIp("127.0.1");
        sysDept.setOperateTime(new Date());
        sysDept.setOperator("user");
        //将值添加进去
        int i = mapper.insertSelective(sysDept);
        if (i == 0) {
            return false;
        }
        return true;
    }


    /**
     * 更新数据
     *
     * @param param
     * @return
     */
    @Override
    public boolean updateDept(SysDeptParam param) {
        //参数校验
        BeanValidator.check(param);

        SysDept olddept = mapper.selectByPrimaryKey(param.getId());
        if (olddept==null){
            throw new ParamException("修改的部门不存在");
        }

            SysDept newDept = SysDept.builder().name(param.getName()).parentId(param.getParentId()).
                    seq(param.getSeq()).remark(param.getRemark()).id(param.getId()).build();

        String mylevel = null;
        if (param.getParentId()==0) {
            mylevel = "0";
        }else {
            mylevel = DeptLevelUtil.getLevel(makevel(param.getParentId()), param.getParentId());
        }
            newDept.setLevel(mylevel);
            return  updateChildDept(olddept,newDept);

    }
     @Transactional
    public boolean updateChildDept(SysDept oldDept,SysDept newDept){
       String newLevel = newDept.getLevel();
         if (!oldDept.getLevel().equals(newLevel)){
             List<SysDept> childDepts = mapper.selectByParentId(oldDept.getId());
             if (!childDepts.isEmpty()){
                 for (SysDept child: childDepts) {
                     SysDept sysDept = new SysDept();
                     BeanUtils.copyProperties(child,sysDept);
                     String level =DeptLevelUtil.getLevel(newLevel,newDept.getId());
                     child.setLevel(level);
                     //递归
                     updateChildDept(sysDept,child);
                 }
             }

         }
         int i = mapper.updateByPrimaryKeySelective(newDept);
         if (i!=0){
             return true;
         }
         return false;
     }

    /**
     * 删除的部门的方法
     * @param param
     * @return
     */

    @Override
    public boolean deleteDept(SysDeptParam param) {
        //获取传过来的id
        //根据这个id来查寻它是否有字集，如果有提示不能越级删除没有就删除
        if (!mapper.selectByParentId(param.getId()).isEmpty()){
            throw new ParamException("不能越级删除");
        }else {
            int i = mapper.deleteByPrimaryKey(param.getId());
            if (i!=0){
                return true;
            }
        }
        return false;
    }

    /*
    添加判断是否存在的方法
     */
    public int checkExit(Integer parentId, String name, Integer id) {
        int i = mapper.checkExit(parentId, name, id);
        return i;
    }

    /*
     赋值level
     */
    public String makevel(Integer deptId) {
        SysDept sysDept = mapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return null;
        }
        return sysDept.getLevel();

    }
}
