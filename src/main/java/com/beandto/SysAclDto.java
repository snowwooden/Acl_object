package com.beandto;

import com.pojo.SysAcl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class SysAclDto extends SysAcl {
    private boolean checked;
    private boolean hasAcl;

    public  static  SysAclDto getAclDto(SysAcl sysAcl){
        SysAclDto dto = new SysAclDto();
        BeanUtils.copyProperties(sysAcl,dto);
        return dto;
    }

}
