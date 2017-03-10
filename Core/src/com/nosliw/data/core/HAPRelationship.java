package com.nosliw.data.core;

import com.nosliw.common.constant.HAPAttribute;

/**
 * This interface describe the related data type
 */
public interface HAPRelationship {

	@HAPAttribute
	public static String PATH = "path";

	@HAPAttribute
	public static String TARGET = "target";

	@HAPAttribute
	public static String SOURCE = "source";
	
	/**
	 * Get target data type that source have relationship with 
	 * @return
	 */
	HAPDataTypeId getTargetDataTypeName();

	/**
	 * Get source data type 
	 * @return
	 */
	HAPDataTypeId getSourceDataTypeName();
	
	
	/**
	 * Get path from source data type to target data type
	 * @return
	 */
	HAPRelationshipPath getPath();
	
}
