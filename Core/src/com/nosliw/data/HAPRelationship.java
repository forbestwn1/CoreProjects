package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;

/**
 * This interface describe the related data type
 */
public interface HAPRelationship {

	@HAPAttribute
	public static String PATH = "path";

	@HAPAttribute
	public static String TARGET = "target";
	
	/**
	 * Get target data type that source have relationship with 
	 * @return
	 */
	HAPDataTypeId getTargetDataTypeName();
	
	/**
	 * Get path from source data type to target data type
	 * @return
	 */
	HAPRelationshipPath getPath();
	
}
