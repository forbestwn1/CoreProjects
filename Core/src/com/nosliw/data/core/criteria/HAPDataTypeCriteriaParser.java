package com.nosliw.data.core.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.UnknownFormatConversionException;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

/**
 * Util class to convert between criteria and literate 
 */
public class HAPDataTypeCriteriaParser {

	private static final String START_AND = "[[";
	private static final String END_AND = "]]";

	private static final String START_OR = "((";
	private static final String END_OR = "))";

	private static final String SEPERATOR_ELEMENT = ",";
	private static final String SEPERATOR_RANGE = "-";
	
	private static final String ANY = "*";
	
	public static String toCriteriaLiterate(HAPDataTypeCriteria criteria){
		String out = null;
		String type = criteria.getType();
		switch(type){
		case HAPConstant.DATATYPECRITERIA_TYPE_AND:
			out = toCriteriaLiterateComplex((HAPDataTypeCriteriaAnd)criteria, START_AND, END_AND);
			break;
		case HAPConstant.DATATYPECRITERIA_TYPE_OR:
			out = toCriteriaLiterateComplex((HAPDataTypeCriteriaOr)criteria, START_OR, END_OR);
			break;
		case HAPConstant.DATATYPECRITERIA_TYPE_DATATYPEID:
			HAPDataTypeCriteriaElementId idCriteria = (HAPDataTypeCriteriaElementId)criteria;
			out = HAPSerializeManager.getInstance().toStringValue(idCriteria.getDataTypeId(), HAPSerializationFormat.LITERATE); 
			break;
		case HAPConstant.DATATYPECRITERIA_TYPE_DATATYPEIDS:
			HAPDataTypeCriteriaElementIds idsCriteria = (HAPDataTypeCriteriaElementIds)criteria;
			out = toCriteriaLiterate(idsCriteria.toOrCriteria());
			break;
		case HAPConstant.DATATYPECRITERIA_TYPE_DATATYPERANGE:
			HAPDataTypeCriteriaElementRange rangeCriteria = (HAPDataTypeCriteriaElementRange)criteria;
			out =  HAPSerializeManager.getInstance().toStringValue(rangeCriteria.getFromDataTypeId(), HAPSerializationFormat.LITERATE)
					+ SEPERATOR_RANGE 
					+ HAPSerializeManager.getInstance().toStringValue(rangeCriteria.getToDataTypeId(), HAPSerializationFormat.LITERATE);
			break;
		case HAPConstant.DATATYPECRITERIA_TYPE_ANY:
			out = ANY;
			break;
		case HAPConstant.DATATYPECRITERIA_TYPE_LITERATE:
			out = ((HAPDataTypeCriteriaWrapperLiterate)criteria).getLiterateValue();
			break;
		}
		
		return out;
	}
	
	private static String toCriteriaLiterateComplex(HAPDataTypeCriteriaComplex complexCriteria, String sepeartorStart, String seperatorEnd){
		StringBuffer andOut = new StringBuffer();
		List<HAPDataTypeCriteria> andEles = complexCriteria.getElements();
		andOut.append(START_AND);
		for(int i=0; i<andEles.size(); i++){
			andOut.append(toCriteriaLiterate(andEles.get(i)));
			if(i<andEles.size()-1)  andOut.append(SEPERATOR_ELEMENT);
		}
		andOut.append(END_AND);
		return andOut.toString();
	}
	
	public static HAPDataTypeCriteria parseLiterate(String criteriaLiterate){
		HAPDataTypeCriteria out = null;
		
		 criteriaLiterate = criteriaLiterate.trim();
		 if(ANY.equals(criteriaLiterate)){
			 out = new HAPDataTypeCriteriaAny();
		 }
		 else if(criteriaLiterate.startsWith(START_AND)){
			 out = new HAPDataTypeCriteriaAnd(parseMutiple(criteriaLiterate.substring(START_AND.length(), criteriaLiterate.length()-END_AND.length())));
		 }
		 else if(criteriaLiterate.startsWith(START_OR)){
			 out = new HAPDataTypeCriteriaAnd(parseMutiple(criteriaLiterate.substring(START_OR.length(), criteriaLiterate.length()-END_OR.length())));
		 }
		 else{
			 String[] criteriaParts = criteriaLiterate.split(SEPERATOR_RANGE);
			 if(criteriaParts.length==1){
				 //id
				 out = new HAPDataTypeCriteriaElementId((HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[0], HAPSerializationFormat.LITERATE));
				 if(out==null)   throw new UnknownFormatConversionException(criteriaLiterate);
			 }
			 else{
				 //range
				 HAPDataTypeId from = null;
				 HAPDataTypeId to = null;
				 if(HAPBasicUtility.isStringNotEmpty(criteriaParts[0])){
					 from = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[0], HAPSerializationFormat.LITERATE);
					 if(from==null)   throw new UnknownFormatConversionException(criteriaLiterate);
				 }
				 
				 if(HAPBasicUtility.isStringNotEmpty(criteriaParts[1])){
					 to = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[1], HAPSerializationFormat.LITERATE);
					 if(to==null)   throw new UnknownFormatConversionException(criteriaLiterate);
				 }
				 out = new HAPDataTypeCriteriaElementRange(from, to);
			 }
		 }
		 
		 return out;
	}
	
	private static List<HAPDataTypeCriteria> parseMutiple(String mutipleLiterate){
		List<HAPDataTypeCriteria> out = new ArrayList<HAPDataTypeCriteria>();
		String[] literates = mutipleLiterate.split(SEPERATOR_ELEMENT);
		
		for(String literate : literates){
			out.add(parseLiterate(literate));
		}
		return out;
	}
}
