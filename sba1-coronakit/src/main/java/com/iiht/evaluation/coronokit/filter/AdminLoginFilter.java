package com.iiht.evaluation.coronokit.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class ExecTimeFilter
 */
@WebFilter("/admin")
public class AdminLoginFilter implements Filter {
	boolean isLoginSuccessful = false;
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(request.getParameter("action").equals("login")) {
			String loginID = request.getParameter("loginid");
			String password = request.getParameter("password");
			if(loginID.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
				System.out.println("Admin login credentials are valid");
				chain.doFilter(request, response);	
				isLoginSuccessful = true;
			}else{
				System.out.println("Admin login credentials are not valid");
				response.setContentType("text/html");
				
				PrintWriter out = response.getWriter();
				out.print("<h3>Admin login credentials are not valid</h3>");
			}
		}else if(isLoginSuccessful) {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
