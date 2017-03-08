package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPDataTypeId;

/**
 * DataTypeCriteria is a way to describe what data type is possible
 * It can be used when 
 * 		define operation parm --- what possible data type is acceptable as parm
 * 		define operation output --- what possible data type will created from operation
 */
@HAPEntityWithAttribute(baseName="DATATYPECRITERIA")
public interface HAPDataTypeCriteria{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	String getType();
	
	boolean validate(HAPDataTypeCriteria criteria);
	
	boolean validate(HAPDataTypeId dataTypeId);
	
	Set<HAPDataTypeId> getValidDataTypeId();
	
	
	
//	String getCriteria();
	
	
	/**
	 * Combine two criteria together
	 * For instance, if two criteria does not compatable with each other, then the result is non
	 * 
	 */
//	void merge(HAPDataTypeCriteria criteria);
	
	
//	public boolean compactable(HAPDataTypeId dataTypeId);
	
	
//	public boolean match(HAPDataTypeCriteria criteria);
	
}
