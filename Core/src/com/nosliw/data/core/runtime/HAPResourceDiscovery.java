package com.nosliw.data.core.runtime;

import java.util.Set;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.expression.HAPExpression;

/**
 * This interface manage all the resources for runtime
 * The implementation depend on runtime env  
 * It provides two function:
 * 		find out all the resources required to execute operation or expression
 * 		prepare resources
 */
public interface HAPResourceDiscovery {

	/**
	 * Discover resources required for data type operation
	 * @param dataTypeInfo
	 * @param dataOpInfo
	 * @return
	 */
	Set<HAPResourceId> discoverResourceRequirement(HAPDataTypeId dataTypeInfo, HAPOperation dataOpInfo);

	/**
	 * Discover resources required for expression 
	 * @param expression
	 * @return
	 */
	Set<HAPResourceId> discoverResourceRequirement(HAPExpression expression);

}
