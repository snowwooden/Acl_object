package com.beanparam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class SysUserParam {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "电话不能为空")
    @Length(message = "长度不大于13字")
    private String telephone;
    @NotBlank(message = "邮箱不能为空")
    @Length(message = "邮箱长度50字以内")
    private String mail;
    @NotNull(message = "必须提供部门id")
    private Integer deptId;
    @NotNull(message = "必须指定用户的状态,1：正常，0：冻结状态，2：删除必须指定用户的状态,1：正常，0：冻结状态，2：删除")
    private Integer status;
    @Length(message = "长度200字以内")
    private String remark;
}
