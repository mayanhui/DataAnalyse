package com.wky.dairyDaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.wky.dairyDao.DairyDao;
import com.wky.dbUtils.DBConnection;

@SuppressWarnings("deprecation")
public class DairyDaoImpl implements DairyDao{

	@Override
	public List<StringBuffer> findDairyByElemTop10(String elem) {
		Connection conn = DBConnection.getConnection();
		String sql = "select dairy.DairyID,dairy.SimpleName,"+elem+" from Dairy  where "+elem+" REGEXP '[0-9]' ORDER BY  "+elem+"  DESC limit 0,10";
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StringBuffer> list = new ArrayList<StringBuffer>();
		StringBuffer sdName = new StringBuffer();
		StringBuffer sdValue = new StringBuffer();
		try{
			ps = conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				sdName.append(rs.getString(2)+",");
				sdValue.append(rs.getString(3)+",");
			}	
			list.add(sdName);
			list.add(sdValue);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnection.close(rs);
			DBConnection.close(ps);
			DBConnection.close(conn);
		}
		return list;
	}

	@Override
	public List<StringBuffer> findDairyByElem(String elem) {
		Connection conn = DBConnection.getConnection();
		String sql = "select dairy.DairyID,dairy.SimpleName,"+elem+" from Dairy  where "+elem+" REGEXP '[0-9]'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StringBuffer> list = new ArrayList<StringBuffer>();
		StringBuffer sdName = new StringBuffer();
		StringBuffer sdValue = new StringBuffer();
		try{
			ps = conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				sdName.append(rs.getString(2)+",");
				sdValue.append(rs.getString(3)+",");
			}	
			list.add(sdName);
			list.add(sdValue);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnection.close(rs);
			DBConnection.close(ps);
			DBConnection.close(conn);
		}
		return list;
	}

	@Override
	public StringBuffer getElemStandard(String elem) {
		StringBuffer st = new StringBuffer();
		return st;
	}

}
