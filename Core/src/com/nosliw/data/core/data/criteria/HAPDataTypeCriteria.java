package com.nosliw.data.core.data.criteria;

import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeId;

/**
 * DataTypeCriteria is a way to describe what data type is possible
 * It can be used when 
 * 		define operation parm --- what possible data type is acceptable as parm
 * 		define operation output --- what possible data type will created from operation
 */
@HAPEntityWithAttribute(baseName="DATATYPECRITERIA")
public interface HAPDataTypeCriteria extends HAPSerializable{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String CHILDREN = "children";

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

	Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper);

	/**
	 * Get all children criteria
	 * @return
	 */
	List<HAPDataTypeCriteria> getChildren();
	
	/**
	 * Find the most general criteria that all the data type that meet normalized criteria should also meet original criteria
	 * either directly or through some converter
	 * @return
	 */
//	HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper);

}
