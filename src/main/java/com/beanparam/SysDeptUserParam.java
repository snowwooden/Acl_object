package com.beanparam;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysDeptUserParam {
    @NotNull(message = "必须要有")
    private Integer depttld;
    private String pageQuery;
    @Length(message = "大于等于一")
    private Integer pageNo;
    @Length(message = "大于等于一")
    private Integer pageSize;
    private String offset;

}
