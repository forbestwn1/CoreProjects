package com.nosliw.data;

import java.util.List;

/**
 * This interface describe the related data type
 */
public interface HAPDataTypeRelationship {

	/**
	 * Get target data type that source have relationship with 
	 * @return
	 */
	HAPDataType getTargetDataType();
	
	/**
	 * Get path from source data type to target data type
	 * @return
	 */
	List<HAPDataTypePathSegment> getPath();
	
}
