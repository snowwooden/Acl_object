package com.controller;

import com.beanparam.SysPageBean;
import com.beanparam.SysUserLoginParam;
import com.beanparam.SysUserParam;
import com.pojo.SysUser;
import com.service.SysUserService;
import com.utils.JsonData;
import com.utils.RequestHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    /**
     * 用户登录
     *
     * @return
     */
    @RequestMapping("login.page")
    public ModelAndView userLogin(SysUserLoginParam param, HttpServletRequest request) {
        SysUser user = sysUserService.login(param);
        request.getSession().setAttribute("user", user);
        ModelAndView view = new ModelAndView();

        view.setViewName("admin");
        return view;
    }

    /**
     * 用户退出
     *
     * @return
     */
    @RequestMapping("logout.page")
    public ModelAndView userLoginOut(HttpServletRequest request) {
        SysUser user = RequestHolder.getUser();
        request.getSession().removeAttribute("user");
        ModelAndView view = new ModelAndView();

        view.setViewName("signin");
        return view;
    }

    /**
     * 新增用户的方法
     */
    @RequestMapping("save.json")
    @ResponseBody
    public JsonData saveUser(SysUserParam param) {
        boolean resoult = sysUserService.savaUser(param);
        return JsonData.success(resoult);
    }

    /**
     * 更新用户的方法
     */
    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateUser(SysUserParam param) {
        boolean resoult = sysUserService.updateUser(param);
        return JsonData.success(resoult);
    }

    /**
     * 展示用户的方法
     */
    @RequestMapping("page.json")
    @ResponseBody
    public JsonData showUsers(Integer deptId, SysPageBean<SysUser> pageBean) {
        SysPageBean<SysUser> page = sysUserService.showUsers(deptId, pageBean);
        return JsonData.success(page);
    }
}
