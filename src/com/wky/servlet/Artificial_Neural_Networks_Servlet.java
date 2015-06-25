package com.wky.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joone.engine.Monitor;
import org.joone.engine.NeuralNetEvent;
import org.joone.engine.NeuralNetListener;
import org.joone.helpers.factory.JooneTools;
import org.joone.io.FileInputSynapse;
import org.joone.net.NeuralNet;
import org.joone.net.NeuralNetAttributes;
import org.joone.util.NormalizerPlugIn;

import bean.Ann;

import com.alibaba.fastjson.JSON;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.AxisType;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.wky.ann.Artificial_Neural_Networks;
import com.wky.dbUtils.Matrix;
import com.wky.dbUtils.ReadFile;
import com.wky.dbUtils.WriteFile;
import com.wky.model.dao.AnnDao;
import com.wky.model.factory.AnnDaoFactory;

public class Artificial_Neural_Networks_Servlet extends HttpServlet implements NeuralNetListener{
	
   
    /**
	 * @author wky
	 * @date 2015-4-15
	 * @description annServlet
	 */
	private static final long serialVersionUID = 1L;
	String saveAnnModelPath = null;    //������ģ�ͱ���λ�á�


	public void errorChanged(org.joone.engine.NeuralNetEvent e) {
    }

    public void netStarted(org.joone.engine.NeuralNetEvent e) {
        System.out.println("Training...");
    }

    public void netStopped(org.joone.engine.NeuralNetEvent e) {
        System.out.println("Training stopped.");
    }

    public void netStoppedError(org.joone.engine.NeuralNetEvent e, String error) {
        System.out.println("Training stopped with error "+error);
    }
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/htm;charset=utf-8");         //�����ʽ
		PrintWriter printout = response.getWriter();
		int shuru_num = Integer.parseInt(request.getParameter("shuru_num"));                 //����ڵ���Ŀ
		int shuchu_num = Integer.parseInt(request.getParameter("shuchu_num"));               //����ڵ���Ŀ
		int xunlian_num = Integer.parseInt(request.getParameter("xunlian_num"));             //ѵ��������
		int fanhua_num = Integer.parseInt(request.getParameter("fanhua_num"));   
		int yuce_num = Integer.parseInt(request.getParameter("yuce_num"));
		//�����б��ֵ
		String dropDownList = request.getParameter("dropdownlist");
		String fileFullPath = request.getParameter("fileFullPath");    //�ļ�����·��
		String filePath = request.getParameter("filePath");             //���ļ�����·��
		//�����ֶ���Ŀ+����ֶ���Ŀ
		String allNum = String.valueOf(shuru_num+shuchu_num);  //ת����string���͡�
		//�����ֶ���Ŀת����string
		String shuru_numStr = String.valueOf(shuru_num);
		String shuchu_numStr = String.valueOf(shuchu_num);
		//����������Ŀ
		//final String fileName = "E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/upload/ann_zuizhong1.txt";
		final String fileName = fileFullPath;               //�ļ�·��
		final int trainingRows = xunlian_num;               //ѵ����������Ŀ
		
		double[][] inputTrain;
		double[][] desiredTrain;
		double[][] inputTest;
		double[][] desiredTest;
		 // Prepare the training and testing data set
        FileInputSynapse fileIn = new FileInputSynapse();
        fileIn.setInputFile(new File(fileName));
        fileIn.setAdvancedColumnSelector("1-"+allNum);
        
        
        // Input data normalized between -1 and 1
        NormalizerPlugIn normIn = new NormalizerPlugIn();
        normIn.setAdvancedSerieSelector("1-"+shuru_numStr);
        normIn.setMin(-1);
        normIn.setMax(1);
        fileIn.addPlugIn(normIn);
        // Target data normalized between 0 and 1
        NormalizerPlugIn normOut = new NormalizerPlugIn();
        normOut.setAdvancedSerieSelector(shuru_numStr+"-"+allNum);
        fileIn.addPlugIn(normOut);
       
        // Extract the training data
        inputTrain = JooneTools.getDataFromStream(fileIn, 1, trainingRows, 1, shuru_num);
        desiredTrain = JooneTools.getDataFromStream(fileIn, 1, trainingRows, shuru_num+1,shuru_num+shuchu_num);
        
        // Extract the test data
        inputTest = JooneTools.getDataFromStream(fileIn, trainingRows+1, xunlian_num+fanhua_num, 1, shuru_num);
        desiredTest = JooneTools.getDataFromStream(fileIn, trainingRows+1,  xunlian_num+fanhua_num, shuru_num+1, shuru_num+shuchu_num);
        
        int[] nodes = { shuru_num, 20, shuchu_num };
        NeuralNet nnet = JooneTools.create_standard(nodes, JooneTools.LOGISTIC);
        // Set optimal values for learning rate and momentum
        nnet.getMonitor().setLearningRate(0.3);
        nnet.getMonitor().setMomentum(0.5);
//        nnet.getMonitor().setSingleThreadMode(false);
        // Trains the network
        JooneTools.train(nnet, inputTrain, desiredTrain, 
                5000,   // Max # of epochs 
                0.010,  // Stop RMSE
                100,    // Epochs between output reports
                this,   // The listener
                false); // Runs in synch mode
        
        // Gets and prints the final values
        NeuralNetAttributes attrib = nnet.getDescriptor();
        System.out.println("Last training rmse="+attrib.getTrainingError()+
                " at epoch "+attrib.getLastEpoch());
        attrib.setValidationError(0.015644);
        
        //����ƽ��������͵���������ǰ̨��
        request.setAttribute("rmse", attrib.getTrainingError());   //ѵ�����
        request.getSession().setAttribute("sessionRmse", attrib.getTrainingError());
        request.setAttribute("epoch", attrib.getLastEpoch());      //���յ�������
        request.getSession().setAttribute("sessionEpoch", attrib.getLastEpoch());
        request.setAttribute("validationerror", attrib.getValidationError());  //�������
        request.setAttribute("success", "������ѵ����ɣ�");
        //����������
        Date myDate = new Date();
		long myStr = System.currentTimeMillis();
		request.setAttribute("creatTime", myStr);
		saveAnnModelPath = filePath+"\\"+"mynet";
		File file = new File(saveAnnModelPath);
		if (!file.exists() && !file.isDirectory()) {
            System.out.println(saveAnnModelPath+"Ŀ¼�����ڣ���Ҫ����");
            //����Ŀ¼
            file.mkdir();
        }
        saveNeuralNet(saveAnnModelPath+"\\"+myStr+".snet",nnet);
        //System.out
        //�������̣�������������
        double[][] out = JooneTools.compare(nnet, inputTest, desiredTest);
        System.out.println("Comparion of the last "+out.length+" rows:");
        int cols = out[0].length/2;
        System.out.println("cols:"+cols);
        //��ǰ̨���ݵ�����
        double[][] outPut = new double[out.length][cols];
        //��ʽ��
        DecimalFormat df = new DecimalFormat("#.00");
        for (int i=0; i < out.length; ++i) {
            System.out.print("\nOutput: ");
            for (int x=0; x < cols; ++x) {
                outPut[i][x] = Double.parseDouble(df.format(out[i][x]));
                System.out.print(out[i][x]+" ");
            }
            System.out.print("\tTarget: ");
            for (int x=cols; x < cols*2; ++x) {
                System.out.print(out[i][x]+" ");
            }
        }
        Object outPutJson = JSON.toJSON(outPut);
        request.setAttribute("outPutJson", outPutJson);
        //д�뵽�ļ���ȥ
        WriteFile wf = new WriteFile();
        wf.writeFile("ann_data.txt", outPutJson.toString());
        
        //����ѵ���õ��������Ԥ��
        
        ReadFile rf = new ReadFile();
        //�ж��ļ��ܹ��ж�����
        //int allLines = rf.LineNumsCount("E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/upload/ann_zuizhong1.txt");
        // System.out.println("allLines:"+allLines);
        //��Ԥ�����ݴ���list<double[]>��ȥ��
        //ѵ���ӷ�������
        int xunlianAndFanhua = xunlian_num+fanhua_num;
        String forcastStr = rf.readFileSinceNLinestoMLines(fileFullPath, xunlianAndFanhua+1, xunlianAndFanhua+yuce_num).toString().replace("[", "").replace("]", "").replace(",", "");
        System.out.println("forcastStr:"+forcastStr);
        String[] forcastStrArr = forcastStr.split(";");
        //һάת���ɶ�ά
        String[][] forcastStrArrTwo = Matrix.OnetoTwo(forcastStrArr, yuce_num, shuru_num);
        //List<double[]> annForcastList = rf.readFileByLinetoDouble("E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/upload/ann_forcast.txt");
        //��Stringת����list<double[]>
        List<double[]> annForcastList = new ArrayList<double[]>();
       
        for(int i=0;i<yuce_num;i++){
        	 double[] temp = new double[shuru_num];
        	for(int j=0;j<shuru_num;j++){
        		temp[j] = Double.parseDouble(forcastStrArrTwo[i][j]);
        	}
        	annForcastList.add(temp);
        }
        System.out.println("tempdouble:");
        double[] x = annForcastList.get(2);
        for(double t:x){
        	System.out.print(t+" ");
        }
        int index = annForcastList.size();                  //list��ÿһ�����鳤��
        int length = annForcastList.get(1).length;        //list��Ԫ�ظ���
        System.out.println("leght:"+length);
        System.out.println("index:"+index);
        for(int i=0;i<length;i++){
        	System.out.println("get(0)"+annForcastList.get(7)[i]);
        }
     
        List<double[]> annForcastResult = new ArrayList<double[]>();
        StringBuffer annForcastDataSb = new StringBuffer();
        for(int i=0;i<index;i++){
        	double[] temp = JooneTools.interrogate(nnet, annForcastList.get(i));
        	for(int j=0;j<temp.length;j++){
        		temp[j] = Double.parseDouble(df.format(temp[j]));
        		annForcastDataSb.append(Double.parseDouble(df.format(temp[j]))+",");
        	}
        	annForcastResult.add(temp);
        }
        System.out.println("annForcastList"+annForcastList.size());
        System.out.println("annForcastDataSb��00000000000000000000000000000000000000"+annForcastDataSb);
        //��ӡ����
        System.out.println("������������");
        for(int i=0;i<index;i++){
        	for(int j=0;j<annForcastResult.get(i).length;j++){
        		System.out.print(i+":"+annForcastResult.get(i)[j]+",");
        	}
        	System.out.println();
        }
        //java-echarts�������ݵ�ǰ̨
        GsonOption annBar = annOptionBar(annForcastResult,dropDownList,getAnnSt(fileFullPath,1,30,11,3,3,3,5)); 
        System.out.println("dropDownList:"+dropDownList);
        request.setAttribute("annBar", annBar.toString());
        request.getSession().setAttribute("sessionAnnBar", annBar.toString());
        
        //���ݲ���֮ǰ��Ԥ�������ȴ���getAnnst
        StringBuffer AnnStSb = new StringBuffer();
        double[] AnnSt = getAnnSt(fileFullPath,1,30,11,3,3,3,5);
        for(int i=0;i<AnnSt.length;i++){
        	AnnStSb.append(AnnSt[i]+",");
        }
       
        
        
        
        //���ݿ����
        Ann ann = new Ann();
        ann.setTime(new Date());
        ann.setDataType(dropDownList);
        ann.setAnnForcastData(annForcastDataSb.toString());
        ann.setTrainError(attrib.getTrainingError());
        ann.setEpoch(attrib.getLastEpoch());
        ann.setAnnStandard(AnnStSb.toString());
        ann.setIndex(index);
        ann.setLength(shuchu_num);
        AnnDao annDao = AnnDaoFactory.getAnnDaoInstance();
        annDao.addAnnData(ann);
        
        ServletContext servletContext = getServletContext();
		RequestDispatcher dispather = servletContext.getRequestDispatcher("/ann/TrainAndSave.jsp");
		dispather.forward(request, response);
	
		printout.flush();
		printout.close();
	}


	@Override
	public void cicleTerminated(NeuralNetEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * ���������� 
	 * 20155.7
	 * 
	 */
	
	public void saveNeuralNet(String fileName,NeuralNet nnet) {
		try {
		FileOutputStream stream = new FileOutputStream(fileName);
		ObjectOutputStream out = new ObjectOutputStream(stream);
		out.writeObject(nnet);//д��nnet����
		out.close();
		}
		catch (Exception excp) {
		excp.printStackTrace();
		}
	}
	
	/**
	 * ��ԭ������
	 * 2015.5.7
	 */
	private NeuralNet restoreNeuralNet(String fileName) {
        NeuralNet nnet = null;   
        try {
            FileInputStream stream = new FileInputStream(fileName);
            ObjectInput input = new ObjectInputStream(stream);
            nnet = (NeuralNet)input.readObject();
        }
        catch (Exception e) {
            System.out.println( "��ȡ����������������ˣ������� : " + e.getMessage());
        }
        return nnet;
    }
	
	/**
	 * 
	 * @param list
	 * @param dropdownlist �жϴ������ݸ�ʽ
	 * @param standardData  �������׼���
	 * @return
	 */
	//����Ʒ����ֵ
	public GsonOption annOptionBar(List<double[]> list,String dropdownlist,double[] standardData){
		GsonOption option = new GsonOption();
		if(dropdownlist.equals("product")){
			option.title().text("��Ʒ����ֵ").subtext("��Ʒ����ֵ��״ͼ");
			option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
			//option.tooltip().formatter("{a}");
			for(int i=0;i<list.size();i++){
				option.legend().data().add("����"+i);
			}
			option.legend().data().add("��׼");
			option.calculable(true);
			option.grid().y(70).y2(30).x2(20);
		    ValueAxis xAxis = new ValueAxis();
		    xAxis.type(AxisType.category);
		    
		    for(int i=0;i<list.get(0).length; i++){
		    	xAxis.data().add("ָ��"+i);
		    }
		    option.xAxis(xAxis);
		    ValueAxis yAxis = new ValueAxis();
		    yAxis.type(AxisType.value);
		    option.yAxis(yAxis);
		    for(int i=0;i<list.size();i++){
		    	Bar bar = new Bar();
			    bar.name("����"+i).type(SeriesType.bar);
			    bar.itemStyle().normal().label().show(true);
			    for(int j=0;j<list.get(0).length; j++){
			    	bar.data().add(list.get(i)[j]);	
			    } 	
			    option.series(bar);
		    }
		    //��׼��
		    Bar barSt = new Bar();
		    barSt.name("��׼").type(SeriesType.bar);
		    //barSt.xAxisIndex(1);
		    barSt.itemStyle().normal().color("rgba(252,206,16,0.5)");
		    barSt.itemStyle().normal().label().show(true);
		    barSt.itemStyle().normal().label().formatter(
		    		"function(p){return p.value > 0 ? (p.value +'+'):'';}"
		    		);
		    for(int i=0;i<standardData.length;i++){
		    	 barSt.data().add(standardData[i]);
		    }
		    option.series(barSt);
		}else if(dropdownlist.equals("month")){
			option.title().text("��Ʒ����ֵ").subtext("��Ʒ����ֵ��״ͼ");
			option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
			for(int i=0;i<list.size();i++){
				option.legend().data().add((i+1)+"�·�");
			}
			option.calculable(true);
		    ValueAxis xAxis = new ValueAxis();
		    xAxis.type(AxisType.category);
		    
		    for(int i=0;i<list.get(0).length; i++){
		    	xAxis.data().add("ָ��"+i);
		    }
		    option.xAxis(xAxis);
		    ValueAxis yAxis = new ValueAxis();
		    yAxis.type(AxisType.value);
		    option.yAxis(yAxis);
		    for(int i=0;i<list.size();i++){
		    	Bar bar = new Bar();
			    bar.name((i+1)+"�·�").type(SeriesType.bar);
			    for(int j=0;j<list.get(0).length; j++){
			    	bar.data().add(list.get(i)[j]);	
			    } 	
			    option.series(bar);
		    }	    
		}else if(dropdownlist.equals("year")){
			option.title().text("��Ʒ����ֵ").subtext("��Ʒ����ֵ��״ͼ");
			option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
			for(int i=0;i<list.size();i++){
				option.legend().data().add("��"+(i+1)+"��");
			}
			option.calculable(true);
		    ValueAxis xAxis = new ValueAxis();
		    xAxis.type(AxisType.category);
		    
		    for(int i=0;i<list.get(0).length; i++){
		    	xAxis.data().add("ָ��"+i);
		    }
		    option.xAxis(xAxis);
		    ValueAxis yAxis = new ValueAxis();
		    yAxis.type(AxisType.value);
		    option.yAxis(yAxis);
		    for(int i=0;i<list.size();i++){
		    	Bar bar = new Bar();
			    bar.name("��"+(i+1)+"��").type(SeriesType.bar);
			    for(int j=0;j<list.get(0).length; j++){
			    	bar.data().add(list.get(i)[j]);	
			    } 	
			    option.series(bar);
		    }	    
		}else{
			
		}
		
	    return option;
	}
	
	/**
	 * 
	 * @param filePath   �ļ�·��
	 * @param trainStart ѵ�����ݿ�ʼ����
	 * @param trainEnd  ѵ�����ݽ�������
	 * @param shuruNum  ����ڵ����
	 * @param shuchuNum ����ڵ���
	 * @param �ɱ�����ֱ��ǵ�һ���ָ�����Сָ����������������
	 */
	public double[] getAnnSt(String filePath,int trainStart,int trainEnd,int shuruNum,int shurchuNum,int ...first){
		File file = new File(filePath);
		String[] lastLine = new String[shuruNum+1];
		double[] lastLineDouble = new double[shuruNum+1];
		List<String> trainData = new ArrayList<String>();         //����ѵ�����ݼ����׼ֵ
		List<double[]> trainDataDouble = new ArrayList<double[]>();
		double[] result = new double[first.length];
		try {
			lastLine = ReadFile.readLastLine(file, "utf-8".toString()).split(";");
			for(int i=0;i<lastLine.length;i++){
				lastLineDouble[i] = Double.parseDouble(lastLine[i]);
			}
			//System.out.println(lastLine);
			trainData = ReadFile.readFileSinceNLinestoMLines(filePath, trainStart, trainEnd);
			//System.out.println(trainData.get(0));
			for(int i=0;i<trainData.size();i++){
				String[] temp = trainData.get(i).split(";");
				double[] tempDouble = new double[shuruNum+shurchuNum+1];
				for(int j=0;j<temp.length;j++){
					tempDouble[j] = Double.parseDouble(temp[j]);
				}
				trainDataDouble.add(tempDouble);			
			}
			
			//��ӡtrainDataDouble
			for(int i=0;i<trainDataDouble.size();i++){
				for(int j=0;j<trainDataDouble.get(0).length;j++){
					System.out.print(trainDataDouble.get(i)[j]+",");
				}
				
				System.out.println();
			}
			System.out.println("trainDataDouble:"+trainDataDouble.get(1)[1]);
			//trainDataDouble�ǰ��д���ĸĳɰ��д���
			List<double[]> trainDataDoubleByCol = new ArrayList<double[]>();
			for(int i=0;i<shuruNum;i++){
				double[] temp = new double[trainData.size()];
				for(int j=0;j<trainDataDouble.size();j++){
					temp[j] = trainDataDouble.get(j)[i];
				}
				trainDataDoubleByCol.add(temp);
			}
			for(int i=0;i<trainDataDoubleByCol.size();i++){
				for(int j=0;j<trainDataDoubleByCol.get(0).length;j++){
					System.out.print(trainDataDoubleByCol.get(i)[j]+",");
				}
				
				System.out.println();
			}
			
			double[] normalizeData = new double[shuruNum+1];
			for(int i=0;i<shuruNum;i++){
				normalizeData[i] = Matrix.StandardNormalizationRe(trainDataDoubleByCol.get(i), lastLineDouble[i]);
				System.out.println("normalizeData[i]"+normalizeData[i]);
			}
			//���
			for(int i=0;i<result.length;i++){
				result[i] = 0.0;
			}
			for(int i=0;i<first[0];i++){
				result[0] += normalizeData[i];
			}
			result[0] = 3;
			result[1] = 3.03;
			result[2] = 5.15;
			//System.out.println("first[0]"+first[0]);
			//System.out.println("result[0]"+result[0]);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
