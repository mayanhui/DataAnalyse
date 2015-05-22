package com.wky.dbUtils;
/**
 * @author wky
 * @date 2015-4-15
 * @description �������
 */
public class Matrix{

	public static double[][] Normalization(double[][] arr){
		int m = arr.length;
		int n = arr[0].length;
		double Max = -1;
		double Min = arr[0][0];
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){
				if(Max<arr[i][j]){
					Max = arr[i][j];
				}
				if(Min>arr[i][j]){
					Min = arr[i][j];
				}
			}
		}
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){
				arr[i][j] = (arr[i][j]-Min)/(Max-Min);
			}
		}
		return arr;
		
	}
	//һά�����һ��
	public static double[] Normalization(double[] arr){
		int m = arr.length;
		double Max = -1;
		double Min = arr[0];
		for(int i=0;i<m;i++){
			if(Max<arr[i]){
				Max = arr[i];
			}
			if(Min>arr[i]){
				Min = arr[i];
			}
		}
		for(int i=0;i<m;i++){
			arr[i] = (arr[i]-Min)/(Max-Min);
		}
		return arr;
		
	}
	//��׼��һ��
	public static double[] StandardNormalization(double[] arr,double standard){
		int m = arr.length;
		double Max = -1;
		double Min = arr[0];
		for(int i=0;i<m;i++){
			if(Max<arr[i]){
				Max = arr[i];
			}
			if(Min>arr[i]){
				Min = arr[i];
			}
		}
		for(int i=0;i<m;i++){
			arr[i] = (standard-Min)/(Max-Min);
		}
		return arr;
	}
	
	//��׼��һ����д
	//��׼��һ��
		public static double StandardNormalizationRe(double[] arr,double standard){
			int m = arr.length;
			double Max = -1;
			double Min = arr[0];
			for(int i=0;i<m;i++){
				if(Max<arr[i]){
					Max = arr[i];
				}
				if(Min>arr[i]){
					Min = arr[i];
				}
			}
			for(int i=0;i<m;i++){
				arr[i] = (standard-Min)/(Max-Min);
			}
			return arr[0];
		}

	//����ת��
	public static double[][] Transposition(double[][] arr){
		
		int m = arr.length;
		int n = arr[0].length;
		double[][] arr1 = new double[n][m];
		for(int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				arr1[i][j] = arr[j][i];
			}
		}
		return arr1;
	}
	
	//һά����ת��Ϊ��ά����,m��n��
	public static String[][] OnetoTwo(String[] arr,int m,int n){
		String[][] tempArr = new String[m][n];
		int length = arr.length;
		int k=0;
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){			
				tempArr[i][j] = arr[k];
			    k++;				
			}
		}
		return tempArr;
	}
	
	//�������
	public static double sumArr(double[] arr,int start,int end){
		double temp = 0.0;
		for(int i=start;i<end;i++){
			temp += arr[i];
		}
		return temp;
	}
}
