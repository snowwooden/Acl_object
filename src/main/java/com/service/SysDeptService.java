package com.service;

import com.beandto.SysDeptDto;
import com.beanparam.SysDeptParam;

import java.util.List;

public interface SysDeptService {
   boolean saveDept(SysDeptParam param);
   boolean updateDept(SysDeptParam param);
   boolean deleteDept(SysDeptParam param);

}
