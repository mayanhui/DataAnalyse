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

public class AddUserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
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
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String password1 = request.getParameter("password1");
		System.out.println(password);
		System.out.println(password1);
		int isAdmin = Integer.parseInt(request.getParameter("isAdmin"));
		if(name==""||"".equals(name)||"".equals(password)||password==""||"".equals(password1)||password1==""){
			request.setAttribute("error", "�û��������벻��Ϊ�գ�");
			dispatcher = servletContext.
					getRequestDispatcher("/UserAdd.jsp");//������תҳ��
			dispatcher.forward(request, response);
		}else if(password.equals(password1)){
			UserImpl userImpl = new UserImpl();
			User user = new User();
			user.setName(name);
			user.setPassword(password);
			user.setIsAdmin(isAdmin);
			int i = userImpl.addUser(user);
			if(i>0){
				request.setAttribute("success", "ע��ɹ���");
				dispatcher = servletContext.
						getRequestDispatcher("/Login.jsp");//������תҳ��
				dispatcher.forward(request, response);
			}else{
				request.setAttribute("error", "duangduangduang!ע��ʧ�ܣ�");
				dispatcher = servletContext.
						getRequestDispatcher("/UserAdd.jsp");//������תҳ��
				dispatcher.forward(request, response);
			}
		}else if(password!=password1){
			request.setAttribute("error", "�������벻һ�£�");
			dispatcher = servletContext.
					getRequestDispatcher("/UserAdd.jsp");//������תҳ��
			dispatcher.forward(request, response);
		}
		
		out.flush();
		out.close();
	}

}
