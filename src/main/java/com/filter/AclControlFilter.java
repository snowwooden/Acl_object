package com.filter;

import com.pojo.SysUser;
import com.utils.JsonData;
import com.utils.JsonMapper;
import com.utils.RequestHolder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
*
 * 当前类没有被Spring管理，
 * 但是在当前类中需要用到被Spring管理的其它类，
 * 所以需要借助ApplicationContextHelper这个帮助类
*/


@Slf4j
public class AclControlFilter implements Filter {

    private final static String noAuthUrl = "/sys/user/noAuth.page";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getRequestURI();
        //判断页面 如果是访问权限提示页面或者登陆页面 则直接放行
        if (servletPath.contains("login")|| servletPath.contains(noAuthUrl)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //判断用户是否登录
        SysUser sysUser = RequestHolder.getUser();
        if (sysUser == null) {
            //为空则直接显示无权限访问
            noAuth(request, response);
            return;
        }
        //用户已登录但是没有相关的一些权限
       /* SysAclCoreService sysRoleCoreService = ApplicationContextHelper.popBean(SysAclCoreService.class);
        if (!sysRoleCoreService.isSuperAdmin(servletPath)) {
            //没有权限也访问无权访问页面
            noAuth(request, response);
            return;
        }*/
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    //没有相关权限时调用的方法
    public void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getRequestURI();
        //划分为俩种情况，json和page访问
        if (servletPath.endsWith(".json")) {
            JsonData jsonData = JsonData.fail("没有访问权限，如需要访问，请联系管理员");
            response.setHeader("Content-Type", "application/json");
            //使用JsonData通过JsonMapper转换为string
            response.getWriter().print(JsonMapper.obj2String(jsonData));
            return;
        } else {
            response.setHeader("Content-Type", "text/html");
            response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                    + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                    + "window.location.href='" + noAuthUrl + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
