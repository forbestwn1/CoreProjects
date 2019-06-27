package com.nosliw.data.core.criteria;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPCriteriaUtility {

	public static final String CHILD_ELEMENT = "element";
	
	
	public static HAPMatchers mergeVariableInfo(HAPVariableInfo variableInfo, HAPDataTypeCriteria expectCriteria, HAPDataTypeHelper dataTypeHelper) {
		if(variableInfo.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN)){
			//if variable info is open, calculate new criteria for this variable
			if(expectCriteria!=null){
				if(variableInfo.getCriteria()==null){
					variableInfo.setCriteria(expectCriteria);
				}
				else{
					HAPDataTypeCriteria adjustedCriteria = dataTypeHelper.merge(variableInfo.getCriteria(), expectCriteria);
					if(adjustedCriteria==null){
						HAPErrorUtility.invalid("cannot merge!!!");
						return null;
					}
					else{
						variableInfo.setCriteria(adjustedCriteria);
					}
				}
			}
		}
		return isMatchable(variableInfo.getCriteria(), expectCriteria, dataTypeHelper);
	}
	
	/**
	 * "And" two criteria and create output. If the "And" result is empty, then set error  
	 * @param criteria
	 * @param expectCriteria
	 * @param context
	 * @return
	 */
	public static HAPMatchers isMatchable(HAPDataTypeCriteria criteria, HAPDataTypeCriteria expectCriteria, HAPDataTypeHelper dataTypeHelper){
		if(expectCriteria==null)   return null;
		
		if(expectCriteria==HAPDataTypeCriteriaAny.getCriteria())   expectCriteria = criteria;
		
		HAPMatchers out = dataTypeHelper.buildMatchers(criteria, expectCriteria);
		if(out==null){
			//not able to match, then error
			HAPErrorUtility.invalid("error!!!");
		}
		return out;
	}
	

	
	public static HAPDataTypeCriteria cloneDataTypeCriteria(HAPDataTypeCriteria criteria) {
		if(criteria==null)  return null;
		String str = criteria.toStringValue(HAPSerializationFormat.LITERATE);
		HAPDataTypeCriteria out = HAPCriteriaParser.getInstance().parseCriteria(str);
		if(criteria instanceof HAPDataTypeCriteriaAbstract) {
			((HAPDataTypeCriteriaAbstract)out).setSolidCriteria(((HAPDataTypeCriteriaAbstract)criteria).getSoldCriteria());
		}
		return out;
	}

	public static HAPDataTypeCriteria getChildCriteriaByPath(HAPDataTypeCriteria criteria, String path) {
		HAPDataTypeCriteria out = criteria;
		HAPPath pathObj = new HAPPath(path);
		for(String pathSeg : pathObj.getPathSegs()) {
			if(out!=null) {
				out = getChildCriteria(out, pathSeg);
			}
		}
		return out;
	}
	
	public static HAPDataTypeCriteria getChildCriteria(HAPDataTypeCriteria criteria, String childName) {
		HAPDataTypeCriteria out = null;
		if(criteria instanceof HAPDataTypeCriteriaWithSubCriteria){
			HAPDataTypeSubCriteriaGroup subGroup = ((HAPDataTypeCriteriaWithSubCriteria)criteria).getSubCriteria();
			if(subGroup!=null)		out = subGroup.getSubCriteria(childName);
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
