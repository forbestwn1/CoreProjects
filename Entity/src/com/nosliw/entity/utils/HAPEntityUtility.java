package com.nosliw.entity.utils;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPDataWraper;

public class HAPEntityUtility {
	
	/*
	 * get critical value which stand for empty/invalid value
	 */
	public static String getEmptyCriticalValue(){return null;}
	
	public static boolean isWraperEmpty(HAPDataWraper wraper){
		if(wraper==null)  return true;
		return wraper.isEmpty();
	}
	
	/*
	 * figure out the scope value based on the parms input for clear up
	 */
	public static int getScopeFromClearupParms(Map<String, Object> parms){
		Integer out = (Integer)parms.get(HAPConstant.WRAPECLEARUP_PARM_SCOPE);
		if(out==null)   return HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL;
		else return out.intValue();
	}
	
	
	
//	public static void (HAPComplexEntity fromEntity, HAPComplexEntity toEntity)
//	{
//		for(HAPAttributeDefinition fromAttr : fromEntity.getEntityInfo().getAttributeDefinitions())
//		{
//			HAPAttributeDefinition toAttr = toEntity.getEntityInfo().getAttributeDefinition(fromAttr.getName());
//			if(toAttr != null){
//				if(attrDef1.equals(attrDef))
//				{
//					String attributeName = attrDef.getName();
//					this.addAttributeValue(attributeName, complexEntity.getAttributeValueWraper(attributeName).clone(false));
//				}
//			}
//		}
//	}
	
}
