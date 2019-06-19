package com.controller;

import com.beanparam.SysAclMoudelParam;
import com.beanparam.SysAclParam;
import com.beanparam.SysPageBean;
import com.pojo.SysAcl;
import com.pojo.SysUser;
import com.service.SysAclMoudelService;
import com.service.SysAclService;
import com.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/acl")
public class SysAclController {

    @Resource
    private SysAclService aclService;

    /**
     * 展示权限点的方法
     */
    @RequestMapping("page.json")
    @ResponseBody
    public JsonData showAcl(Integer aclModuleId, SysPageBean<SysAcl> pageBean) {
        SysPageBean<SysAcl> acl = aclService.showAcl(aclModuleId, pageBean);
        return JsonData.success(acl);
    }

    /**
     * 新增一个权限点
     * @return
     */
    @RequestMapping("save.json")
    @ResponseBody
    public JsonData savaAcl(SysAclParam param){
        boolean b = aclService.saveAcl(param);
        return JsonData.success(b);
    }
    /**
     * 更新一个权限点
     * @return
     */
    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateAcl(SysAclParam param){
        boolean b = aclService.updateAcl(param);
        return JsonData.success(b);
    }

    /**
     * 删除一个权限点
     * @return
     */
    @RequestMapping("delete.json")
    @ResponseBody
    public JsonData deleteAcl(SysAclParam param) {
        boolean b = aclService.deleteAcl(param);
        return JsonData.success(b);
    }

}
