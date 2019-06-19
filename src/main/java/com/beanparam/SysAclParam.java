package com.beanparam;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SysAclParam {
    private Integer id;
    @NotBlank(message = "部门名字不能为空且长度再2-15之间")
    private String name;
    private Integer aclModuleId;
    @NotNull(message = "级别顺序不能为空")
    private Integer seq;
    @Length(message = "最大长度不超过150字")
    private String  remark;

    private Integer status;

    private String url;

    private Integer type;
}
