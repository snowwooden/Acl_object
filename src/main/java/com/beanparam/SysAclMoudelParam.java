package com.beanparam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@ToString
public class SysAclMoudelParam {
    private  Integer id;
    @NotBlank(message = "用户名不能为空,长度2-15个字")
    private String name;
    private  Integer parentId;
    @NotNull(message = "不可为空")
    private Integer seq;
    @NotNull(message = "1：正常 0:冻结")
    private  Integer status;
    @NotNull(message = "150字以内")
    private String remark;

}
