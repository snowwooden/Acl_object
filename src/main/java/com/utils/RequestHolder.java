package com.utils;


import com.pojo.SysUser;

import javax.servlet.http.HttpServletRequest;
public class RequestHolder {
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<SysUser>();
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();


    public static void add(SysUser sysUser){
        userHolder.set(sysUser);
    }
    public static void add(HttpServletRequest req){
        requestHolder.set(req);
    }

    public static SysUser getUser(){
        return userHolder.get();
    }
    public static HttpServletRequest getRequest(){
        return requestHolder.get();
    }

    //解除绑定
    public static void remove(){
        userHolder.remove();
        requestHolder.remove();
    }

}
