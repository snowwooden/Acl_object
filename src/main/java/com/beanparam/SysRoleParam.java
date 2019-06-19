package com.beanparam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleParam {

    private Integer id;
    @NotBlank(message = "名字不能为空")
    private String name;

    private  String  remark;

    private Integer status;

    private Integer type=1;//todo:前端问题
}
