package com.wky.dairyDao;

import java.util.List;

import bean.DairyBean;

/**
 * @author wky
 */
//�ҳ�dairy����ĳԪ�غ����ı仯����
public interface DairyDao {
	public List<StringBuffer> findDairyByElemTop10(String elem);
	public List<StringBuffer> findDairyByElem(String elem);
	public StringBuffer getElemStandard(String elem);
	public List<StringBuffer> findDairyByElemAndPlace(String elem,String place);
	
}
