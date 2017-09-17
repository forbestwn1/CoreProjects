package com.nosliw.data.core.runtime;

import java.util.List;

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
	 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
	 */
	List<HAPResourceInfo> discoverResourceRequirement(HAPDataTypeId dataTypeInfo, HAPOperation dataOpInfo);

	/**
	 * Discover resources required for expression 
	 * @param expression
	 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
	 */
	List<HAPResourceInfo> discoverResourceRequirement(List<HAPExpression> expression);

	/**
	 * Discover resource information (dependency)
	 * @param resourceIds
	 * @return
	 */
	List<HAPResourceInfo> discoverResource(List<HAPResourceId> resourceIds);
	
}
