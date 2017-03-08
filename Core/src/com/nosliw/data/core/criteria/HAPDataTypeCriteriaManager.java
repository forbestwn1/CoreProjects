package com.nosliw.data.core.criteria;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaManager {

	private static final String START_AND = "[[";
	private static final String END_AND = "]]";

	private static final String START_OR = "((";
	private static final String END_OR = "))";
	
	public static HAPDataTypeCriteria and(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2){
		return null;
	}

	public static HAPDataTypeCriteria or(HAPDataTypeCriteria criteria1, HAPDataTypeCriteria criteria2){
		return null;
	}

	public static boolean compatibleWith(HAPDataTypeCriteria check, HAPDataTypeCriteria base){
		
	}
	
	public static boolean compatibleWith(HAPDataTypeId check, HAPDataTypeCriteria base){
		
	}
	
	public HAPDataTypeCriteria parseLiterate(String criteriaLiterate){
		HAPDataTypeCriteria out = null;
		
		 criteriaLiterate = criteriaLiterate.trim();
		 if(criteriaLiterate.startsWith(START_AND)){
			 out = new HAPDataTypeCriteriaAnd(parseMutiple(criteriaLiterate.substring(START_AND.length(), criteriaLiterate.length()-END_AND.length())));
		 }
		 else if(criteriaLiterate.startsWith(START_OR)){
			 out = new HAPDataTypeCriteriaAnd(parseMutiple(criteriaLiterate.substring(START_OR.length(), criteriaLiterate.length()-END_OR.length())));
		 }
		 else{
			 String[] criteriaParts = criteriaLiterate.split("-");
			 if(criteriaParts.length==1){
				 //id
				 out = new HAPDataTypeCriteriaElementId((HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[0], HAPSerializationFormat.LITERATE));
			 }
			 else{
				 //range
				 HAPDataTypeId from = null;
				 HAPDataTypeId to = null;
				 if(HAPBasicUtility.isStringNotEmpty(criteriaParts[0])){
					 from = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[0], HAPSerializationFormat.LITERATE);
				 }
				 
				 if(HAPBasicUtility.isStringNotEmpty(criteriaParts[1])){
					 to = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[1], HAPSerializationFormat.LITERATE);
				 }
				 out = new HAPDataTypeCriteriaElementRange(from, to);
			 }
		 }
		 
		 return out;
	}
	
	private List<HAPDataTypeCriteria> parseMutiple(String mutipleLiterate){
		List<HAPDataTypeCriteria> out = new ArrayList<HAPDataTypeCriteria>();
		String[] literates = mutipleLiterate.split(",");
		
		for(String literate : literates){
			out.add(this.parseLiterate(literate));
		}
		return out;
	}
}
