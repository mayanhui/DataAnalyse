package com.wky.dairyDao;

import java.util.List;

import bean.DairyBean;

/**
 * @author wky
 * @deprecated ����dairy���һ�в���
 */
//�ҳ�dairy����ĳԪ�غ����ı仯����
public interface DairyDao {
	public List<StringBuffer> findDairyByElemTop10(String elem);
	public List<StringBuffer> findDairyByElem(String elem);
	public StringBuffer getElemStandard(String elem);
}
