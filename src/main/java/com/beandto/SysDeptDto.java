package com.beandto;

import com.pojo.SysDept;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型转换
 */
@Getter
@Setter
public class SysDeptDto extends SysDept {

      List<SysDeptDto> deptList = new ArrayList<>();

    public  static  SysDeptDto ChangeToDto(SysDept sysDept) {
        SysDeptDto sysDeptDto = new SysDeptDto();
        //调用BeanUtil来转换
        BeanUtils.copyProperties(sysDept, sysDeptDto);
        return sysDeptDto;
    }
}
