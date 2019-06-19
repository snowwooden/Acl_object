package com.beanparam;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

public class SysPageBean<T> {
    @Getter
    @Setter
    @Min(value = 0,message = "页码不能为空")
    private int pageNo;
    @Getter
    @Setter
    @Min(value = 0,message = "不可为空")
    private int pageSize=1;

    public int getOffset() {
        return offset=(pageNo-1)*pageSize;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private int offset;
    @Getter
    @Setter
    private int total = 0;

    @Getter
    @Setter
    private List<T> data = new ArrayList<>();

}
