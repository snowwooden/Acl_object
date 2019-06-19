package com.beanparam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@ToString
public class SysDeptParam {

    private Integer id;
    @NotBlank(message = "部门名字不能为空且长度再2-15之间")
    private String name;
    private Integer parentId = 0;
    @NotNull(message = "级别顺序不能为空")
    private Integer seq;
    @Length(message = "最大长度不超过150字")
    private String  remark;
}
