package com.wky.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.joone.helpers.factory.JooneTools;
import org.joone.net.NeuralNet;

import com.alibaba.fastjson.JSON;
import com.wky.dbUtils.ReadFile;
import com.wky.dbUtils.WriteFile;

public class ImportAnnForcastServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");		
		PrintWriter printOut = response.getWriter();	      
		 //�õ��ϴ��ļ��ı���Ŀ¼�����ϴ����ļ������WEB-INFĿ¼�£����������ֱ�ӷ��ʣ���֤�ϴ��ļ��İ�ȫ
	      String savePath = this.getServletContext().getRealPath("/WEB-INF/anndata/userforcastmodel");
	      File file = new File(savePath);
	      //�ж��ϴ��ļ��ı���Ŀ¼�Ƿ����
	      if (!file.exists() && !file.isDirectory()) {
	          System.out.println(savePath+"Ŀ¼�����ڣ���Ҫ����");
	          //����Ŀ¼
	          file.mkdir();
	      }
	      //��Ϣ��ʾ
	      String message = "";
	      String fileFullPath="";
	      String fileName = "";
	      String filePath = "";            //�����ļ���·����û���ļ���
	      List<FileItem> list = null;
	      try{
	          //ʹ��Apache�ļ��ϴ���������ļ��ϴ����裺
	          //1������һ��DiskFileItemFactory����
	          DiskFileItemFactory factory = new DiskFileItemFactory();
	          //2������һ���ļ��ϴ�������
	          ServletFileUpload upload = new ServletFileUpload(factory);
	           //����ϴ��ļ�������������
	          upload.setHeaderEncoding("UTF-8"); 
	          //3���ж��ύ�����������Ƿ����ϴ���������
	          if(!ServletFileUpload.isMultipartContent(request)){
	              //���մ�ͳ��ʽ��ȡ����
	              return;
	          }
	          //4��ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>���ϣ�ÿһ��FileItem��Ӧһ��Form����������
	          list = upload.parseRequest(request);
	          for(FileItem item : list){
	              //���fileitem�з�װ������ͨ�����������
	              if(item.isFormField()){
	                  String name = item.getFieldName();
	                  //�����ͨ����������ݵ�������������
	                  String value = item.getString("UTF-8");
	                  //value = new String(value.getBytes("iso8859-1"),"UTF-8");
	                  System.out.println(name + "=" + value);
	              }else{//���fileitem�з�װ�����ϴ��ļ�
	                  //�õ��ϴ����ļ����ƣ�
	                  String filename = item.getName();
	                  fileName = filename;
	                  System.out.println(filename);
	                  if(filename==null || filename.trim().equals("")){
	                      continue;
	                  }
	                  //ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺  c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt
	                  //�����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
	                  filename = filename.substring(filename.lastIndexOf("\\")+1);
	                  //��ȡitem�е��ϴ��ļ���������
	                  InputStream in = item.getInputStream();
	                  //����һ���ļ������
	                  FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
	                  fileFullPath = savePath+"\\"+filename;
	                  filePath = savePath;                    //�����ļ�·��
	                  //����һ��������
	                  byte buffer[] = new byte[1024];
	                  //�ж��������е������Ƿ��Ѿ�����ı�ʶ
	                  int len = 0;
	                  //ѭ�������������뵽���������У�(len=in.read(buffer))>0�ͱ�ʾin���滹������
	                  while((len=in.read(buffer))>0){
	                      //ʹ��FileOutputStream�������������������д�뵽ָ����Ŀ¼(savePath + "\\" + filename)����
	                      out.write(buffer, 0, len);
	                  }
	                  //�ر�������
	                  in.close();
	                  //�ر������
	                  out.close();
	                  //ɾ�������ļ��ϴ�ʱ���ɵ���ʱ�ļ�
	                  item.delete();
	                  message = "�ļ��ϴ��ɹ���";
	              }
	          }
	      }catch (Exception e) {
	          message= "�ļ��ϴ�ʧ�ܣ�";
	          e.printStackTrace();
	          
	      }
	    NeuralNet nnet = null;			
		try {
			nnet = JooneTools.load(filePath+"\\"+list.get(0).getName());
	  		ReadFile rf = new ReadFile();
	  		List<double[]> forcastArr = rf.readFileByLinetoDouble(filePath+"\\"+list.get(1).getName());
			System.out.println(forcastArr.size());
			int index = forcastArr.size();       //list��Ԫ�ظ���
			int length = forcastArr.get(1).length;
			//����
			List<double[]> annForcastResult = new ArrayList<double[]>();
			DecimalFormat df = new DecimalFormat("#.00");
			for(int i=0;i<index;i++){
	        	double[] temp = JooneTools.interrogate(nnet, forcastArr.get(i));
	        	for(int j=0;j<temp.length;j++){
	        		temp[j] = Double.parseDouble(df.format(temp[j]));
	        	}
	        	annForcastResult.add(temp);
		     }
			System.out.println("������������");
	        for(int i=0;i<index;i++){
	        	for(int j=0;j<annForcastResult.get(i).length;j++){
	        		System.out.print(i+":"+annForcastResult.get(i)[j]+",");
	        	}
	        	System.out.println();
	        }
	        //���͵�ǰ��
	        //�������������ļ�
	      	WriteFile wf = new WriteFile();
	        Object annForcastResultJson = JSON.toJSON(annForcastResult);
	        wf.writeFile("annForcastResult.txt", annForcastResultJson.toString());
	        request.setAttribute("annForcastResultJson", annForcastResultJson);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}			
       
		request.getRequestDispatcher("/ann/import_ann_forcast.jsp").forward(request, response);
		printOut.flush();
		printOut.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		doGet(request,response);
		out.flush();
		out.close();
	}

}
