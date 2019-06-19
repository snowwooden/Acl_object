package com.service;

import com.beandto.AclModuleLevelDto;
import com.beandto.SysAclDto;
import com.beandto.SysDeptDto;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.mapper.SysAclMapper;
import com.mapper.SysAclModuleMapper;
import com.mapper.SysDeptMapper;
import com.pojo.SysAcl;
import com.pojo.SysAclModule;
import com.pojo.SysDept;
import com.google.common.collect.Multimap;
import com.utils.DeptLevelUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SysDeptTreeService {
    //注入mappr
    @Resource
    private SysDeptMapper deptMapper;
    //注入SysAclModuleMapper 权限模块
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    //注入角色权限service
    private SysAclCoreService sysRoleCoreService;

    @Resource
    //注入权限
    private SysAclMapper sysAclMapper;
    @Resource
    private SysAclCoreService sysAclCoreService;

    /**
     * 生成部门树
     *
     * @return
     */
    //将全部实体类转换成dto
    public List<SysDeptDto> getDeptTree() {
        //首先获取所有的部门信息
        List<SysDept> allDept = deptMapper.getAllDept();
        List<SysDeptDto> sysDeptDtos = new ArrayList<>();
        //转换为dto
        for (SysDept dept : allDept) {
            SysDeptDto dto = SysDeptDto.ChangeToDto(dept);
            sysDeptDtos.add(dto);
        }
        return createTreeList(sysDeptDtos);//TODO
    }

    //将所有的dto便利出来更具level分成顶层数据（用list装） 子层数据（用MuilMap装起来 因为这个map对同kay数据是list不是覆盖）
    public List<SysDeptDto> createTreeList(List<SysDeptDto> dtos) {
        //判空执行接下来的 操作
        if (dtos.isEmpty()) {
            return null;
        }
        //创建list来装顶层数据
        List<SysDeptDto> rootDepts = new ArrayList<>();
        //创建Muilmap来装子层数据
        Multimap<String, SysDeptDto> childDepts = ArrayListMultimap.create();
        //根据level来判断顶层数据还是子层数据并装进对应的集合
        for (SysDeptDto dto : dtos) {
            if (DeptLevelUtil.ROOT.equals(dto.getLevel())) {
                rootDepts.add(dto);
            }
            childDepts.put(dto.getLevel(), dto);
        }
        //调用下边的方法来分层
        Collections.sort(rootDepts, new MyComparator());
        createRecTree(rootDepts, childDepts);
        return rootDepts;
    }

    //将者两层数据进行递归得到递归树
    public void createRecTree(List<SysDeptDto> rootDepts, Multimap<String, SysDeptDto> childDepts) {
        //获取顶岑该数据的level和id
        for (int i = 0; i < rootDepts.size(); i++) {
            SysDeptDto dto = rootDepts.get(i);
            String level = DeptLevelUtil.getLevel(dto.getLevel(), dto.getId());
            //下边的根据这个level来得到下边的数据
            List<SysDeptDto> dtos = (List<SysDeptDto>) childDepts.get(level);
            if (dtos != null) {
                //排序
                Collections.sort(dtos, new MyComparator());
                //设置下一层数据
                dto.setDeptList(dtos);
                //递归调用
                createRecTree(dtos, childDepts);
            }
        }


    }

    /**
     * 生成权限树
     */
    //获取所有的权限数据 权限模块树
    public List<AclModuleLevelDto> createAclModelTree() {
        //查询出所有权限
        List<SysAclModule> aclModuleList = sysAclModuleMapper.findAclModel();
        //创建一个dto的集合 存储权限
        List<AclModuleLevelDto> aclModuleDtoList = new ArrayList<>();
        //将查询出来的acl转换为dto
        for (SysAclModule sysAclModule : aclModuleList) {
            AclModuleLevelDto dto = AclModuleLevelDto.adapt(sysAclModule);
            aclModuleDtoList.add(dto);
        }
        return aclModuleDtoListToTree(aclModuleDtoList);
    }

    //封装dto 将数据封装到权限树中
    public List<AclModuleLevelDto> aclModuleDtoListToTree(List<AclModuleLevelDto> aclModuleList) {
        if (aclModuleList.isEmpty()) {
            return aclModuleList;
        }
        //按权限封装数据
        Multimap<String, AclModuleLevelDto> map = ArrayListMultimap.create();
        //创建集合存储顶层部门
        List<AclModuleLevelDto> rootList = new ArrayList<>();
        //遍历集合封装数据
        for (AclModuleLevelDto dto : aclModuleList) {
            if (DeptLevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
            map.put(dto.getLevel(), dto);
        }
        //对同级部门进行排序 根据seq
        Collections.sort(rootList, aclModuleSeqComparator);
        //调用递归树
        recAclModuleTree(rootList, map);
        return rootList;
    }

    //递归生成树
    //递归 recursion(rec)
    public void recAclModuleTree(List<AclModuleLevelDto> rootList, Multimap<String, AclModuleLevelDto> map) {
        //遍历每一个元素
        for (int i = 0; i < rootList.size(); i++) {
            AclModuleLevelDto moduleLevelDto = rootList.get(i);
            //获取当前层的数据
            String key = DeptLevelUtil.getLevel(moduleLevelDto.getLevel(), moduleLevelDto.getId());//0.1
            //处理顶层下的数据
            List<AclModuleLevelDto> aclModuleLevelDtos = (List<AclModuleLevelDto>) map.get(key);
            if (!aclModuleLevelDtos.isEmpty()) {
                //排序 顶层的下层数据排序
                Collections.sort(aclModuleLevelDtos, aclModuleSeqComparator);
                //设置下一层数据
                moduleLevelDto.setAclModuleList(aclModuleLevelDtos);
                //递归调用
                recAclModuleTree(aclModuleLevelDtos, map);
            }
        }
    }

    /**
     * 生成角色权限树
     */
    public List<AclModuleLevelDto> creatAclRoleTree(int roleId) {
        //获取用户的权限点信息
        List<SysAcl> aclbyuserId = sysRoleCoreService.getAclsByUserId();
        //获取角色的权限点信息
        List<SysAcl> aclByRoleId = sysRoleCoreService.getAclByRoleId(roleId);
        //获取全部权限信息
        List<SysAcl> sysAclList = sysAclMapper.selecAllAcl();
        //用一个权限dto来装
        List<SysAclDto> dtos = Lists.newArrayList();
        for (SysAcl acl : sysAclList) {
            SysAclDto dto = SysAclDto.getAclDto(acl);
            // 判断用户权限中是否包含这个权限点
            // 注意到SysAcl中重写 @EqualsAndHashCode(of = {"id","code"})
            if (aclbyuserId.contains(acl)) {
                dto.setHasAcl(true);
            }
            // 判断角色权限中是否包含这个权限点
            if (aclByRoleId.contains(acl)) {
                dto.setChecked(true);
            }
            //将满足条件封装到所有权限中
            dtos.add(dto);
        }
        return aclListToTree(dtos);
    }
    //将创建的数据封装到模块树中
    public List<AclModuleLevelDto> aclListToTree(List<SysAclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        //将权限点封装到对应的权限模块中
        //获取权限模块树
        List<AclModuleLevelDto> aclModuleLevelList = createAclModelTree();

        Multimap<Integer, SysAclDto> moduleIdAclMap = ArrayListMultimap.create();

        for(SysAclDto acl : aclDtoList) {
            //判断权限的状态是否有效
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }
        //递归生成树
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }
    //绑定权限点和权限模块树
    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, SysAclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }
        //遍历模块树
        for (AclModuleLevelDto dto : aclModuleLevelList) {
            //通过权限模块id从map集合中取出权限点集合
            List<SysAclDto> aclDtoList = (List<SysAclDto>)moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                //排序
                Collections.sort(aclDtoList, aclSeqComparator);
                //将权限点集合设置到权限模块树中
                dto.setAclList(aclDtoList);
            }
            //递归调用
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }
    }


    public class MyComparator implements Comparator<SysDeptDto> {

        @Override
        public int compare(SysDeptDto o1, SysDeptDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    }

    //权限根据seq排序的方法
    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
    //权限点根据seq排序的方法
    public Comparator<SysAclDto> aclSeqComparator = new Comparator<SysAclDto>() {
        @Override
        public int compare(SysAclDto o1, SysAclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
