package com.nosliw.data;

/**
 * This interface manage all the resources for runtime
 * The implementation depend on runtime env  
 * It provides two function:
 * 		find out all the resources required to execute operation or expression
 * 		prepare resources
 */
public interface HAPResourceManager {

	/**
	 * what runtime env this resource manager represent
	 * @return
	 */
	public HAPRuntimeInfo getRuntimeInfo();
	
	/**
	 * Discover resources required for data type operation
	 * @param dataTypeInfo
	 * @param dataOpInfo
	 * @return
	 */
	public HAPResources discoverResourceRequirement(HAPDataTypeId dataTypeInfo, HAPOperation dataOpInfo);

	/**
	 * Discover resources required for expression 
	 * @param expression
	 * @return
	 */
	public HAPResources discoverResourceRequirement(HAPExpression expression);

	/**
	 * Prepare the actual resource
	 * @param resources
	 * @return
	 */
	public HAPResources prepareResources(HAPResources resources);

}
