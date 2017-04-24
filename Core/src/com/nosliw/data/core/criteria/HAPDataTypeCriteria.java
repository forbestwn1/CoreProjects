package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPDataTypeHelper;
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
	
	/**
	 * Whether criteria is compatible with this criteria
	 * @param criteria
	 * @return
	 */
	boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper);
	
	/**
	 * Whether data type meet the criteria 
	 * @param criteria
	 * @return
	 */
	boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper);
	
	/**
	 * Get all the valid data type ids that is valid for this criteria
	 * @return
	 */
	Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper);

	/**
	 * Find the most general criteria that all the data type that meet normalized criteria should also meet original criteria
	 * either directly or through some converter
	 * @return
	 */
	HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper);
}
