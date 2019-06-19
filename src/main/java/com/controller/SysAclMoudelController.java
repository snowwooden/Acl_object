package com.controller;

import com.beanparam.SysAclMoudelParam;
import com.service.SysAclMoudelService;
import com.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/aclModule")
public class SysAclMoudelController {

    @Resource
    private SysAclMoudelService moudelService;

    /**
     * 进入权限
     * @return
     */
    @RequestMapping("/acl.page")
    public ModelAndView enterAcl() {
        ModelAndView view = new ModelAndView();
        view.setViewName("acl");
        return view;
    }


    /**
     * 新增一个权限模块
     * @return
     */
    @RequestMapping("save.json")
    @ResponseBody
    public JsonData savaAclMoudel(SysAclMoudelParam param){
        boolean b = moudelService.saveAclMoudel(param);
        return JsonData.success(b);
    }
    /**
     * 更新一个权限模块
     * @return
     */
    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateAclMoudel(SysAclMoudelParam param){
        boolean b = moudelService.updateAclMoudel(param);
        return JsonData.success(b);
    }

    /**
     * 删除一个模块
     * @return
     */
    @RequestMapping("delete.json")
    @ResponseBody
    public JsonData deleteAclMoudel(Integer id) {
        boolean b = moudelService.deleteAclMoudel(id);
        return JsonData.success(b);
    }
    /**id
     * 展示权限模块树
     * @return
     */
    @RequestMapping("tree.json")
    @ResponseBody
    public JsonData showAclMoudelTree(){
        List<Map> maps = moudelService.showAclMoudelTree();
        return JsonData.success(maps);
    }
}
