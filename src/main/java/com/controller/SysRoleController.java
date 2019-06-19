package com.controller;

import com.beandto.AclModuleLevelDto;
import com.beanparam.SysRoleParam;
import com.pojo.SysRole;
import com.pojo.SysUser;
import com.service.SysDeptTreeService;
import com.service.SysRoleService;
import com.service.SysRoleUserService;
import com.service.impl.SysRoleServiceImpl;
import com.utils.JsonData;
import com.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
    @Resource
    private SysRoleService roleService;
    @Resource
    private SysDeptTreeService sysDeptTreeService;

    @Resource
    private SysRoleUserService sysRoleUserService;
    /**
     * 进入角色模块
     */
    @RequestMapping("/role.page")
    public ModelAndView enterAcl() {
        ModelAndView view = new ModelAndView();
        view.setViewName("role");
        return view;
    }
    /**
     * 新增一个角色
     * @return
     */
    @RequestMapping("save.json")
    @ResponseBody
    public JsonData savaAclMoudel(SysRoleParam param){
        boolean b = roleService.saveRole(param);
        return JsonData.success(b);
    }
    /**
     * 修改一个角色
     * @return
     */
    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateAclMoudel(SysRoleParam param){
        boolean b = roleService.updateRole(param);
        return JsonData.success(b);
    }
    /**id
     * 展示角色表
     * @return
     */
    @RequestMapping("list.json")
    @ResponseBody
    public JsonData showAclMoudelTree(){
        List<SysRole> sysRoles = roleService.showRoleList();
        return JsonData.success(sysRoles);
    }
    /**
     *角色权限树
     */
    @RequestMapping("roleTree.json")
    @ResponseBody
    public JsonData showAcRoleTree(int roleId){
        List<AclModuleLevelDto> dtos = sysDeptTreeService.creatAclRoleTree(roleId);
        return JsonData.success(dtos);
    }
    /**
     * 更新角色权限树
     */
    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        roleService.updateAclRoleTree(roleId, aclIdList);
        return JsonData.success();
    }
    /**
     * 角色与用户
     */
    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData shouRoleUsers(@RequestParam("roleId") int roleId) {
        return JsonData.success(sysRoleUserService.getUserMapByRoleId(roleId));
    }
    /**
     * 角色用户更新
     */
    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        return JsonData.success();
    }

}
