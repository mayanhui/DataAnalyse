package com.wky.dbUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadFileTest {
	public static void main(String[] arsgs){
		/*
		ReadFile rf = new ReadFile();
		List<String> list = rf.readFileByConfirmLinetoList1("E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/userdata/ahp_weight.txt",2);
		System.out.println(list.toString().replace("[", "").replace("]", " "));
		Iterator it = list.iterator();
		while(it.hasNext()){
			System.out.println(it.next().toString().replace("[", "").replace("]", ""));
			//System.out.println(Double.parseDouble(it.next().toString().replace("[", "").replace("]", " "))+" ");
		}
		
		//RandomAccessFile ����
		File file = new File("E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/userdata/annForcastResult.txt");
		String lastLine = null;
		try {
			lastLine = rf.readLastLine(file, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(lastLine);
		
		//���Դӵڶ��п�ʼ��ȡ����
		System.out.println("���Դӵڶ��п�ʼ��ȡ���ݣ�");
		List<String> list2 = rf.readFileSince2toList("E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/upload/ahp_data.txt");
		System.out.println(list2.toString().replace("[", "").replace("]", " "));
		System.out.println(rf.LineElemNumsCount("E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/upload/ahp_data.txt"));
		List<String> list3 = rf.readFileByConfirmLinetoList("E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/upload/ahp_data.txt", 1);
		System.out.println(list3.toString().replace("[", "").replace("]", " "));
		
		//��ȡ���ݵ�n�е���m��
		System.out.println("���Զ�ȡ���ݵ�n�е���m�е����ݣ�");
		List<String> list4 = rf.readFileSinceNLinestoMLines("E:/JAVA/apache-tomcat-5.5.27/apache-tomcat-5.5.27/webapps/DataAnalyse/WEB-INF/upload/ann_zuizhong1.txt", 39, 46);
		System.out.println(list4.toString().replace("[", "").replace("]", " "));
	}*/
		String filePath = "C:/Users/wky/Desktop/���ݵ���/ann_zuizhong1.txt";
		getAnnSt(filePath,1,30,11,3,3,3,5);
		
		Matrix m = new Matrix();
		double[] arr = {1,2,3,4,5};
		double[] brr = m.StandardNormalization(arr, 5);
		System.out.println(brr[4]+"dasfa");
		System.out.println(brr[0]+"adsfa");
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
	public static void getAnnSt(String filePath,int trainStart,int trainEnd,int shuruNum,int shurchuNum,int ...first){
		File file = new File(filePath);
		String[] lastLine = new String[shuruNum+1];
		double[] lastLineDouble = new double[shuruNum+1];
		List<String> trainData = new ArrayList<String>();         //����ѵ�����ݼ����׼ֵ
		List<double[]> trainDataDouble = new ArrayList<double[]>();
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
			double[] result = new double[first.length];
			for(int i=0;i<result.length;i++){
				result[i] = 0.0;
			}
			for(int i=0;i<first[0];i++){
				result[0] += normalizeData[i];
			}
			result[0] = 3;
			result[1] = 3.03;
			result[2] = 5.15;
			System.out.println("first[0]"+first[0]);
			System.out.println("result[0]"+result[0]);
			//return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
