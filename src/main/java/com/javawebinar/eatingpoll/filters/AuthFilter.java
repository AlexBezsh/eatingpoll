package com.javawebinar.eatingpoll.filters;

import com.javawebinar.eatingpoll.model.user.Role;
import com.javawebinar.eatingpoll.transfer.UserDto;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/user/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        HttpSession session = servletRequest.getSession(false);
        if (session == null
                || session.getAttribute("user") == null
                || (((UserDto) session.getAttribute("user")).isAdmin() && !servletRequest.getServletPath().startsWith("/admin"))
                || (((UserDto) session.getAttribute("user")).getRole() == Role.USER && !servletRequest.getServletPath().startsWith("/user"))) {
            request.setAttribute("message", "You have no access to requested page. Please sigh up or log in");
            servletRequest.getServletContext().getRequestDispatcher(servletRequest.getContextPath() + "/").forward(servletRequest, servletResponse);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }
}
