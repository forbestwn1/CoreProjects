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
	public static String TYPE = "type";

	String getType();
	
	boolean validate(HAPDataTypeCriteria criteria);
	
	boolean validate(HAPDataTypeId dataTypeId);
	
	Set<HAPDataTypeId> getValidDataTypeId();

	HAPDataTypeCriteria normalize();
}
