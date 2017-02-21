package com.nosliw.data;

/**
 * This interface describe the related data type
 */
public interface HAPRelationship {

	/**
	 * Get target data type that source have relationship with 
	 * @return
	 */
	HAPDataType getTargetDataType();
	
	/**
	 * Get path from source data type to target data type
	 * @return
	 */
	HAPRelationshipPath getPath();
	
}
