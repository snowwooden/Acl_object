package com.filter;

import com.pojo.SysUser;
import com.utils.RequestHolder;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取url
        String uri = request.getRequestURI();
        if(uri.contains("login")){

            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            SysUser user = (SysUser) request.getSession().getAttribute("user");
            if(user==null){
                response.sendRedirect("/signin.jsp");
                return;
            }else{
                RequestHolder.add(user);
                RequestHolder.add(request);
                SysUser user1 = RequestHolder.getUser();
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
