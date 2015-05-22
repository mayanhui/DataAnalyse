package com.wky.dbUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

public class TestExcelUtil {
	public static void main(String[] args){
			try {
				
				ExcelUtil eu = new ExcelUtil();
				eu.setExcelPath("E:/ʳƷ��ȫ�����ھ�/ʳƷ���ݷ���/AHP/AHP����/ahp_data.xls");
				
				//System.out.println("=======����Excel Ĭ�� ��ȡ========");
				List<Row> list = eu.readExcel();
				//����
				int rows = list.size();
				int cols = list.get(1).getPhysicalNumberOfCells();
				System.out.println("rows:"+rows);
				System.out.println("cols:"+cols);
				Row r = list.get(0);  //ĳһ��
				StringBuffer sb0 = new StringBuffer();
				Iterator it = r.cellIterator();
				while(it.hasNext()){
					sb0.append(it.next()+",");
				}
				System.out.println("��һ�У�");
				System.out.println(sb0);
				//��ȡ��һ��
				StringBuffer sb = new StringBuffer();
				for(int i=1;i<rows;i++){
					Row r1 = list.get(i);   //ÿһ��
					sb.append(r1.getCell(0)+",");
				}
				System.out.println("��һ�У�");
				System.out.println(sb);
	
				
				//��ȡ�м�������
				StringBuffer sb1 = new StringBuffer();
				int index = 1;
				for(int i=1;i<rows;i++){
					Row r2 = list.get(i);
					for(int j=1;j<cols;j++){
						sb1.append(r2.getCell(j)+",");
						index ++;
					}
				}
				String str = sb1.toString();
				System.out.println("Str:"+str);
				System.out.println("�м����ݣ�");
				System.out.println(sb1);
				System.out.println("index:"+index);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
