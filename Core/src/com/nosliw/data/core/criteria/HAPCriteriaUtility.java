package com.nosliw.data.core.criteria;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPCriteriaUtility {

	public static final String CHILD_ELEMENT = "element";
	
	public static HAPDataTypeCriteria cloneDataTypeCriteria(HAPDataTypeCriteria criteria) {
		if(criteria==null)  return null;
		String str = criteria.toStringValue(HAPSerializationFormat.LITERATE);
		return HAPCriteriaParser.getInstance().parseCriteria(str);
	}
	
	public static HAPDataTypeCriteria getChildCriteria(HAPDataTypeCriteria criteria, String childName) {
		HAPDataTypeCriteria out = null;
		if(criteria instanceof HAPDataTypeCriteriaWithSubCriteria){
			if(((HAPDataTypeCriteriaWithSubCriteria)criteria).getSubCriteria()==null) {
				int kkkk = 5555;
				kkkk++;
			}
			
			out = ((HAPDataTypeCriteriaWithSubCriteria)criteria).getSubCriteria().getSubCriteria(childName);
		}
		return out;
	}

	public static HAPDataTypeCriteria getElementCriteria(HAPDataTypeCriteria criteria) {
		return getChildCriteria(criteria, CHILD_ELEMENT);
	}
	
	public static String[][] mapping = {
		{":", ";;;"},
		{",", ";;"}
	};
	
	public static String escape(String content){
		String out = content;
		for(int i=0; i<mapping.length; i++){
			out = out.replaceAll(mapping[i][0], mapping[i][1]);
		}
		return out;
	}
	
	public static String deescape(String content){
		String out = content;
		for(int i=0; i<mapping.length; i++){
			out = out.replaceAll(mapping[i][1], mapping[i][0]);
		}
		return out;
	}
	
	  public static HAPDataTypeCriteria parseCriteria(String criteria){
		  return HAPCriteriaParser.getInstance().parseCriteria(criteria);
	  }	
}
