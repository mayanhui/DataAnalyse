package com.wky.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wky.drawcharts.SentimentPieWeibo;

import bean.DairyBean;
import bean.Weibo;

import dbImpl.ShowAll;
import dbImpl.ShowAllElemAnalyse;
import dbImpl.ShowWeibo;
/*
 * Author:wky
 * Description:�ۺ�չʾservlet,��ԭ�е�ShowAllServlet��������
 * Time:2015.1.13
 * */
public class ShowAllByHotWordServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * �ַ���������һ��Ҫ�ŵ���ǰ�档
		 * */
		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/htm;charset=utf-8");         //�����ʽ
		PrintWriter out = response.getWriter();
		//��ȡǰ̨����
		String name = request.getParameter("dairyname");
		String city = request.getParameter("selectcity");
		System.out.print("name:"+name+" "+"city"+city);
		ShowAll showAll = new ShowAll();
		List<DairyBean> listDairyBean = showAll.getInfo(name, city);
		request.setAttribute("listDairyBean", listDairyBean);
		
		//echarts ͼ�����ݴ���
		ShowAllElemAnalyse showAllElemAnalyse = new ShowAllElemAnalyse();
		List<DairyBean> listProtein = showAllElemAnalyse.getProteinAnalyse(city, name);
		StringBuffer Protein_name = new StringBuffer();
		StringBuffer Protein_value = new StringBuffer();
		for(DairyBean l:listProtein){
			Protein_name.append(l.getSimpleName()+"("+l.getDairyID()+")"+",");
			Protein_value.append(l.getProteinRe()+",");
		}
		request.setAttribute("Protein_name", Protein_name);
		request.setAttribute("Protein_value", Protein_value);
		System.out.println("Protein_name"+Protein_name);
		System.out.println("Protein_value"+Protein_value);
		
		List<DairyBean> listAflatoxin = showAllElemAnalyse.getAflatoxinAnalyse(city, name);
		StringBuffer Aflatoxin_name = new StringBuffer();
		StringBuffer Aflatoxin_value = new StringBuffer();
		for(DairyBean l:listAflatoxin){
			Aflatoxin_name.append(l.getSimpleName()+"("+l.getDairyID()+")"+",");
			Aflatoxin_value.append(l.getAflatoxinRe()+",");
		}
		request.setAttribute("Aflatoxin_name", Aflatoxin_name);
		request.setAttribute("Aflatoxin_value", Aflatoxin_value);
		System.out.println("Aflatoxin_name"+Aflatoxin_name);
		System.out.println("Aflatoxin_value"+Aflatoxin_value);
		
		//�������۲���
		ShowWeibo showWeibo = new ShowWeibo();
		List<Weibo> weibolist = showWeibo.getWeibo(name);
		request.setAttribute("weibolist", weibolist);
		//��ͼ
		SentimentPieWeibo senPieWeibo = new SentimentPieWeibo();
		int good = senPieWeibo.getCount(name, 1);
		int bad = senPieWeibo.getCount(name, -1);
		int neutral = senPieWeibo.getCount(name, 0);
		StringBuffer pie_value = new StringBuffer();
		pie_value.append(good+",");
		pie_value.append(bad+",");
		pie_value.append(neutral);
		request.setAttribute("Piearray", pie_value);
		request.setAttribute("keyname", name);//  �����Ĺؼ���
		ServletContext servletContext = getServletContext();
		RequestDispatcher dispather = servletContext.getRequestDispatcher("/ShowAll/ShowAllByHotWordNew.jsp");
		dispather.forward(request, response);
		out.flush();
		out.close();
	}

}
