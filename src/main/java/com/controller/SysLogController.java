package com.controller;

import com.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/log")
public class SysLogController {
    @RequestMapping("log.page")
    public ModelAndView mostLog(){
         return new ModelAndView("log");
    }
}
