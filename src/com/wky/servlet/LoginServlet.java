package com.wky.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;

import dbImpl.UserImpl;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=uft-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		doPost(request,response);
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		ServletContext servletContext = getServletContext();	//���ServletContex
		RequestDispatcher dispatcher = null;					
		int id = Integer.parseInt(request.getParameter("id"));
		String password = request.getParameter("password");
		int status = Integer.parseInt(request.getParameter("status"));
		System.out.println(status);
		if(password==""||"".equals(id)||"".equals("password")){
			request.setAttribute("error", "�û���������Ϊ�գ�");
			dispatcher = servletContext.
					getRequestDispatcher("/Login.jsp");//������תҳ��
			dispatcher.forward(request, response);
		}else{
			UserImpl userImpl = new UserImpl();
			User user = userImpl.findUserByID(id);
			//System.out.println(user.getIsAdmin());
			if(user==null){
				request.setAttribute("error", "�����ڸ��û���");
				dispatcher = servletContext.
						getRequestDispatcher("/Login.jsp");//������תҳ��
				dispatcher.forward(request, response);
			}else{
				if(password.equals(user.getPassword())&status==user.getIsAdmin()&user.getIsAdmin()==1){
					request.getSession().
					setAttribute("user", user);//��Ա����Ϣ���浽session��Χ
					request.getSession().setAttribute("status", "1");
					response.sendRedirect("index.jsp");
					return;
				}else if(password.equals(user.getPassword())&status==user.getIsAdmin()&user.getIsAdmin()==0){
					request.getSession().
					setAttribute("user", user);//��Ա����Ϣ���浽session��Χ
					request.getSession().setAttribute("status", "0");
					response.sendRedirect("index.jsp");
					return;
				}else{
					request.setAttribute("error", "ϵͳ�����ȷ!");
					dispatcher = servletContext.
						getRequestDispatcher("/Login.jsp");
					dispatcher.forward(request, response);
				}
			}
		}
		out.flush();
		out.close();
	}

}
