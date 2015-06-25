package com.wky.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.AHP;

import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.AxisType;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.wky.model.dao.AHPDao;
import com.wky.model.factory.AHPDaoFactory;

public class AHPDetailsServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
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
		String id = request.getParameter("id");
		System.out.println(id);
		if(id!=null){
			AHPDao ahpDao = AHPDaoFactory.getAprioriDaoInstance();
			AHP ahp = new AHP();
			ahp = ahpDao.findAHPById(Integer.parseInt(id));
			GsonOption weightOptionBar = WeightOptionBar(ahp.getRowName().split(","),ArrStrToArrDou(ahp.getWeight().split(",")));
			GsonOption productRiskOptionBar = productRiskOptionBar(ahp.getColName().split(","),ArrStrToArrDou(ahp.getProductRisk().split(",")),ahp.getProductRiskSt());
			GsonOption weightOptionPie = WeightOptionPie(ahp.getRowName().split(","),ArrStrToArrDou(ahp.getWeight().split(",")));
			request.setAttribute("weightOptionBar", weightOptionBar.toString());
			request.setAttribute("productRiskOptionBar", productRiskOptionBar.toString());
			request.setAttribute("weightOptionPie", weightOptionPie.toString());
		}else{
			
		}
		ServletContext servletContext = getServletContext();          
    	RequestDispatcher dispather = servletContext.getRequestDispatcher("/ahp/ahp_details.jsp");
    	dispather.forward(request, response);
		out.flush();
		out.close();
	}
	
	public GsonOption WeightOptionBar(String[] rowName,double[] weight){
		GsonOption option = new GsonOption();
		option.title().text("��Ʒ���ָ��Ȩ��ֵ").subtext("��Ʒ���ָ��Ȩ����״ͼ");
		option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
		option.legend().data().add("Ȩ��");
		
		option.calculable(true);
		
	    ValueAxis xAxis = new ValueAxis();
	    xAxis.type(AxisType.category);
	    
	    for(int i=0;i<rowName.length; i++)
	    	xAxis.data().add(rowName[i]);
	    option.xAxis(xAxis);
	    ValueAxis yAxis = new ValueAxis();
	    yAxis.type(AxisType.value);
	    option.yAxis(yAxis);

	    Bar bar = new Bar();
	    bar.name("Ȩ��").type(SeriesType.bar);
	    for(int i=0;i<weight.length; i++)
	    	bar.data().add(weight[i]);	 
	    option.series(bar);
	    return option;
	}
	
	//��Ȩֵ��ͼ
	public GsonOption WeightOptionPie(String[] rowName,double[] weight){
		//System.out.println("jsonMap:"+jsonMap);
		GsonOption option = new GsonOption();
		option.title().text("��Ʒ���ָ��Ȩ��ֵ����").subtext("��Ʒ���ָ��Ȩ�ر�ͼ");
		option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
		for(int i=0;i<rowName.length; i++)
			option.legend().data().add(rowName[i]);
	    Pie pie = new Pie();
	    pie.name("Ȩ��ֵ").type(SeriesType.pie).center("50%","45%").radius("50%"); 
	    for(int i=0;i<weight.length;i++){
	    	Map map = new HashMap();
	    	map.put("value",weight[i]);
	    	map.put("name",rowName[i]);
	    	pie.data().add(map);
	    }
	    option.series(pie);
	    return option;
	}
	
	//����Ʒ����ֵ
	public GsonOption productRiskOptionBar(String[] colName,double[] productRisk,double productRiskSt){
		GsonOption option = new GsonOption();
		option.title().text("��Ʒ����ֵ").subtext("��Ʒ����ֵ��״ͼ");
		option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
		option.legend().data().add("����ֵ");
		option.legend().data().add("�����ٽ�ֵ");
		option.calculable(true);
		
	    ValueAxis xAxis = new ValueAxis();
	    xAxis.type(AxisType.category);
	    
	    for(int i=0;i<colName.length; i++)
	    	xAxis.data().add(colName[i]);
	    option.xAxis(xAxis);
	    ValueAxis yAxis = new ValueAxis();
	    yAxis.type(AxisType.value);
	    option.yAxis(yAxis);

	    Bar bar = new Bar();
	    Line line = new Line();
	    bar.name("����ֵ").type(SeriesType.bar);
	    line.name("�����ٽ�ֵ").type(SeriesType.line);
	    for(int i=0;i<productRisk.length; i++){
	    	bar.data().add(productRisk[i]);	
	    	line.data().add(productRiskSt);
	    }
	    	
	    option.series(bar,line);
	    return option;
	}
	
	//String[] to double[] 
	private double[] ArrStrToArrDou(String[] str){
		double[] temp = new double[str.length];
		for(int i=0;i<str.length;i++){
			temp[i] = Double.parseDouble(str[i]);
		}
		return temp;
	}

}
