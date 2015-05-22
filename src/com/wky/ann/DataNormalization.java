package com.wky.ann;

import java.util.List;

import com.wky.dbUtils.Matrix;
import com.wky.dbUtils.ReadFile;

/**
 * 
 * @author wky
 * @date 2015-4-15
 * @description ʵ�����ݹ�һ��
 */
public class DataNormalization {
	//��ԭʼ���ݽ��й�һ�������õ����������
	public static void DataNormalize(){
		String fileName = "E:/ʳƷ��ȫ�����ھ�/ʳƷ���ݷ���/������ģ��/ʵ������/ann.txt";
		//ÿһ��Ԫ�ظ���
		int lineElemNums = ReadFile.LineElemNumsCount(fileName);
		int lines = ReadFile.LineNumsCount(fileName);
		System.out.println("lines:"+lines);
		System.out.println("lineElemNums��"+lineElemNums);
		//��̬����ֱ������������
		List<String> dataList = ReadFile.readFileByLinestoList(fileName);
		int k=0;
		double [][]b = new double[lines][lineElemNums];
		for(int i=0;i<lines;i++){
			for(int j=0;j<lineElemNums;j++){
				b[i][j] = Double.parseDouble(dataList.get(k++));
			}
		}
		//��b����ת��
		double[][] b_zhuanzhi = new double[lineElemNums][lines];
		b_zhuanzhi = Matrix.Transposition(b);
		//�洢��һ���������
		double[][] guiyihua = new double[lineElemNums][lines];
		for(int i=0;i<lineElemNums;i++){
			guiyihua[i] = Matrix.Normalization(b_zhuanzhi[i]);
		}
		for(int i=0;i<lineElemNums;i++){
			System.out.println();
			for(int j=0;j<lines;j++){
				System.out.print(guiyihua[i][j]+",");
			}
		}
		/**
		double[] arr = Matrix.Normalization(b_zhuanzhi[0]);
		for(double temp:arr){
			System.out.println(temp);
		}*/
		//������һ�����ָ����ӵõ����
		//�ٴ�ת�á���
		//�ȶ�ǰ����ָ���һ����Ӻͣ����Ӧ�ÿ�������ͨ�õġ�
		double[][] c_zhuanzhi = new double[lines][lineElemNums];
		c_zhuanzhi = Matrix.Transposition(guiyihua);
		double[] zhibiaoshuchu = new double[lines];
		for(int i=0;i<lines;i++){
			for(int j=6;j<11;j++){
				zhibiaoshuchu[i] += c_zhuanzhi[i][j];
			}
			
		}
		System.out.println();
		System.out.println("ǰ����ָ��");
		for(double temp:zhibiaoshuchu){
			System.out.println(temp);
		}
	}
	//��ÿһ�еı�׼ֵ���й�һ���õ���׼���
	public static void StandardDataNormalize(){
		System.out.println("�Ա�׼���ݵĹ�һ����ʼ�ˡ�");
		String fileName = "E:/ʳƷ��ȫ�����ھ�/ʳƷ���ݷ���/������ģ��/ʵ������/ann.txt";
		//ÿһ��Ԫ�ظ���
		int lineElemNums = ReadFile.LineElemNumsCount(fileName);
		int lines = ReadFile.LineNumsCount(fileName);
		System.out.println("lines:"+lines);
		System.out.println("lineElemNums��"+lineElemNums);
		//��̬����ֱ������������
		List<String> dataList = ReadFile.readFileByLinestoList(fileName);
		int k=0;
		double [][]b = new double[lines][lineElemNums];
		for(int i=0;i<lines;i++){
			for(int j=0;j<lineElemNums;j++){
				b[i][j] = Double.parseDouble(dataList.get(k++));
			}
		}
		//��b����ת��
		double[][] b_zhuanzhi = new double[lineElemNums][lines];
		b_zhuanzhi = Matrix.Transposition(b);
		//�洢��һ���������
		double[][] guiyihua = new double[lineElemNums][lines];
		//�����еĹ�һ��11������
		guiyihua[0] = Matrix.StandardNormalization(b_zhuanzhi[0], 3);
		guiyihua[1] = Matrix.StandardNormalization(b_zhuanzhi[1], 50);
		guiyihua[2] = Matrix.StandardNormalization(b_zhuanzhi[2], 3);
		guiyihua[3] = Matrix.StandardNormalization(b_zhuanzhi[3], 0.3);
		guiyihua[4] = Matrix.StandardNormalization(b_zhuanzhi[4], 5);
		guiyihua[5] = Matrix.StandardNormalization(b_zhuanzhi[5], 0.2);
		guiyihua[6] = Matrix.StandardNormalization(b_zhuanzhi[6], 0.1);
		guiyihua[7] = Matrix.StandardNormalization(b_zhuanzhi[7], 0.2);
		guiyihua[8] = Matrix.StandardNormalization(b_zhuanzhi[8], 0.5);
		guiyihua[9] = Matrix.StandardNormalization(b_zhuanzhi[9], 20);
		guiyihua[10] = Matrix.StandardNormalization(b_zhuanzhi[10], 0.2);
		
		//������һ�����ָ����ӵõ����
		//�ٴ�ת�á���
		//�ȶ�ǰ����ָ���һ����Ӻͣ����Ӧ�ÿ�������ͨ�õġ�
		double[][] c_zhuanzhi = new double[lines][lineElemNums];
		c_zhuanzhi = Matrix.Transposition(guiyihua);
		double[] zhibiaoshuchu = new double[lines];
		for(int i=0;i<lines;i++){
			for(int j=6;j<11;j++){
				zhibiaoshuchu[i] += c_zhuanzhi[i][j];
			}
			
		}
		System.out.println();
		System.out.println("ǰ����ָ��");
		for(double temp:zhibiaoshuchu){
			System.out.println(temp);
		}
		System.out.println("�Ա�׼���ݵĹ�һ�������ˡ�");
	}
	public static void main(String[] args){
		//DataNormalize();
		StandardDataNormalize();
	}
}
