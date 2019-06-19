package com.controller;

import com.beandto.SysDeptDto;
import com.beanparam.SysDeptParam;
import com.pojo.SysUser;
import com.service.SysDeptService;
import com.service.SysDeptTreeService;
import com.utils.JsonData;
import com.utils.RequestHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysDeptTreeService treeService;

    @RequestMapping("/dept.page")
    public ModelAndView SysShowAcl() {
        ModelAndView view = new ModelAndView();
        view.setViewName("dept");
        return view;
    }

    /**
     * dept部门树查询
     */
    @RequestMapping("tree.json")
    @ResponseBody
    public JsonData SysShowDeptTree(HttpServletRequest request) {
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        SysUser user1 = RequestHolder.getUser();
        List<SysDeptDto> dtos = treeService.getDeptTree();
        return JsonData.success(dtos);
    }

    /**
     * 新增加部门方法
     */
    @RequestMapping("save.json")
    @ResponseBody
    public JsonData saveDept(SysDeptParam param) {
        //调用service的方法执行保存
        boolean flg = sysDeptService.saveDept(param);
        return JsonData.success(flg);
    }

    /**
     * 更新数据方法
     */
    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateDept(SysDeptParam param) {
        //调用service的方法执行保存
        boolean flg = sysDeptService.updateDept(param);
        return JsonData.success(flg);
    }
    /**
     * 更新数据方法
     */
    @RequestMapping("delete.json")
    @ResponseBody
    public JsonData deleteDept(SysDeptParam param) {
        //调用service的方法执行保存
       boolean flg = sysDeptService.deleteDept(param);
        return JsonData.success(flg);
    }
}
