package com.wky.apriori;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Apriori2 {
	private final static String ITEM_SPLIT=";"; // ��֮��ķָ���
    private final static String CON="->"; // ��֮��ķָ���
    public Map<String,Integer> getFC(List<String> transList,int SUPPORT){
        Map<String,Integer> frequentCollectionMap=new HashMap<String,Integer>();//���е�Ƶ����
 
        frequentCollectionMap.putAll(getItem1FC(transList,SUPPORT));
 
        Map<String,Integer> itemkFcMap=new HashMap<String,Integer>();
        itemkFcMap.putAll(getItem1FC(transList,SUPPORT));
        while(itemkFcMap!=null&&itemkFcMap.size()!=0){
          Map<String,Integer> candidateCollection=getCandidateCollection(itemkFcMap);
          Set<String> ccKeySet=candidateCollection.keySet();
 
          //�Ժ�ѡ��������ۼӼ���
          for(String trans:transList){
             for(String candidate:ccKeySet){
                      boolean flag=true;// �����жϽ������Ƿ���ָú�ѡ�������֣�������1
                      String[] candidateItems=candidate.split(ITEM_SPLIT);
                      for(String candidateItem:candidateItems){
                               if(trans.indexOf(candidateItem+ITEM_SPLIT)==-1){
                                         flag=false;
                                         break;
                               }
                      }
                      if(flag){
                               Integer count=candidateCollection.get(candidate);
                               candidateCollection.put(candidate, count+1);
                      }
             }
          }
 
          //�Ӻ�ѡ�����ҵ�����֧�ֶȵ�Ƶ������
          itemkFcMap.clear();
          for(String candidate:ccKeySet){
             Integer count=candidateCollection.get(candidate);
             if(count>=SUPPORT){
                 itemkFcMap.put(candidate, count);
             }
          }
 
          //�ϲ�����Ƶ����
          frequentCollectionMap.putAll(itemkFcMap);
 
        }
 
        return frequentCollectionMap;
 }


 private Map<String,Integer> getCandidateCollection(Map<String,Integer> itemkFcMap){
           Map<String,Integer> candidateCollection=new HashMap<String,Integer>();
           Set<String> itemkSet1=itemkFcMap.keySet();
           Set<String> itemkSet2=itemkFcMap.keySet();

           for(String itemk1:itemkSet1){
                    for(String itemk2:itemkSet2){
                             //��������
                             String[] tmp1=itemk1.split(ITEM_SPLIT);
                             String[] tmp2=itemk2.split(ITEM_SPLIT);

                             String c="";
                             if(tmp1.length==1){
                                       if(tmp1[0].compareTo(tmp2[0])<0){
                                                c=tmp1[0]+ITEM_SPLIT+tmp2[0]+ITEM_SPLIT;
                                       }
                             }else{
                                       boolean flag=true;
            for(int i=0;i<tmp1.length-1;i++){
                   if(!tmp1[i].equals(tmp2[i])){
                            flag=false;
                            break;
                   }
            }
            if(flag&&(tmp1[tmp1.length-1].compareTo(tmp2[tmp2.length-1])<0)){
                   c=itemk1+tmp2[tmp2.length-1]+ITEM_SPLIT;
            }
                             }

                             //���м�֦
                             boolean hasInfrequentSubSet = false;
                             if (!c.equals("")) {
                                       String[] tmpC = c.split(ITEM_SPLIT);
                                       for (int i = 0; i < tmpC.length; i++) {
                                                String subC = "";
                                                for (int j = 0; j < tmpC.length; j++) {
                                                         if (i != j) {
                                                                   subC = subC+tmpC[j]+ITEM_SPLIT;
                                                         }
                                                }
                                                if (itemkFcMap.get(subC) == null) {
                                                         hasInfrequentSubSet = true;
                                                         break;
                                                }
                                       }
                             }else{
                                       hasInfrequentSubSet=true;
                             }

                             if(!hasInfrequentSubSet){
                                       candidateCollection.put(c, 0);
                             }
                    }
           }

           return candidateCollection;
 }


 private Map<String,Integer> getItem1FC(List<String>transList,int SUPPORT){
           Map<String,Integer> sItem1FcMap=new HashMap<String,Integer>();
           Map<String,Integer> rItem1FcMap=new HashMap<String,Integer>();//Ƶ��1�

           for(String trans:transList){
                    String[] items=trans.split(ITEM_SPLIT);
                    for(String item:items){
                             Integer count=sItem1FcMap.get(item+ITEM_SPLIT);
                             if(count==null){
                                       sItem1FcMap.put(item+ITEM_SPLIT, 1);
                             }else{
                                       sItem1FcMap.put(item+ITEM_SPLIT, count+1);
                             }
                    }
           }

           Set<String> keySet=sItem1FcMap.keySet();
           for(String key:keySet){
                    Integer count=sItem1FcMap.get(key);
                    if(count>=SUPPORT){
                             rItem1FcMap.put(key, count);
                    }
           }
           return rItem1FcMap;
 }


 public Map<String,Double> getRelationRules(Map<String,Integer> frequentCollectionMap,double CONFIDENCE){
           Map<String,Double> relationRules=new HashMap<String,Double>();
           Set<String> keySet=frequentCollectionMap.keySet();
           for (String key : keySet) {
                    double countAll=frequentCollectionMap.get(key);
                    String[] keyItems = key.split(ITEM_SPLIT);
                    if(keyItems.length>1){
                             List<String> source=new ArrayList<String>();
                             Collections.addAll(source, keyItems);
                             List<List<String>> result=new ArrayList<List<String>>();

                             buildSubSet(source,result);//���source�����зǿ��Ӽ�

                             for(List<String> itemList:result){
            if(itemList.size()<source.size()){//ֻ�������Ӽ�
                   List<String> otherList=new ArrayList<String>();
                   for(String sourceItem:source){
                            if(!itemList.contains(sourceItem)){
                                     otherList.add(sourceItem);
                            }
                   }
                String reasonStr="";//ǰ��
                String resultStr="";//���
                for(String item:itemList){
                        reasonStr=reasonStr+item+ITEM_SPLIT;
                }
                for(String item:otherList){
                        resultStr=resultStr+item+ITEM_SPLIT;
                }

                double countReason=frequentCollectionMap.get(reasonStr);
                double itemConfidence=countAll/countReason;//�������Ŷ�
                if(itemConfidence>=CONFIDENCE){
                        String rule=reasonStr+CON+resultStr;
                        relationRules.put(rule, itemConfidence);
                }
            }
                             }
                    }
           }

           return relationRules;
 }


 private  void buildSubSet(List<String> sourceSet, List<List<String>> result) {
           // ����һ��Ԫ��ʱ���ݹ���ֹ����ʱ�ǿ��Ӽ���Ϊ����������ֱ����ӵ�result��
           if (sourceSet.size() == 1) {
                    List<String> set = new ArrayList<String>();
                    set.add(sourceSet.get(0));
                    result.add(set);
           } else if (sourceSet.size() > 1) {
                    // ����n��Ԫ��ʱ���ݹ����ǰn-1���Ӽ�������result��
                    buildSubSet(sourceSet.subList(0, sourceSet.size() - 1), result);
                    int size = result.size();// �����ʱresult�ĳ��ȣ����ں����׷�ӵ�n��Ԫ��ʱ����
                    // �ѵ�n��Ԫ�ؼ��뵽������
                    List<String> single = new ArrayList<String>();
                    single.add(sourceSet.get(sourceSet.size() - 1));
                    result.add(single);
                    // �ڱ���ǰ���n-1�Ӽ�������£��ѵ�n��Ԫ�طֱ�ӵ�ǰn���Ӽ��У������µļ����뵽result��;
                    // Ϊ����ԭ��n-1���Ӽ���������Ҫ�ȶ�����и���
                    List<String> clone;
                    for (int i = 0; i < size; i++) {
                             clone = new ArrayList<String>();
                             for (String str : result.get(i)) {
                                       clone.add(str);
                             }
                             clone.add(sourceSet.get(sourceSet.size() - 1));

                             result.add(clone);
                    }
           }
 }
}
