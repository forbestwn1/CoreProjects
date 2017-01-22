package com.nosliw.data;

import java.util.Set;

/**
 * Interface that contains all the related data type for source data type
 * Each related data type means that source data type can be converted to that data type through some path
 * Each data type relationship contains two information: target data type and conversion path 
 */
public interface HAPDataTypePicture {

	public HAPDataType getSourceDataType();

	public HAPDataTypeRelationship getRelationship(HAPDataTypeInfo dataTypeInfo);

	public Set<? extends HAPDataTypeRelationship> getRelationships();
	
}
