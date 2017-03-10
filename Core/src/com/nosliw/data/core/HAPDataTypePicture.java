package com.nosliw.data.core;

import java.util.Set;

/**
 * Interface that contains all the related data type for source data type
 * Each related data type means that source data type can be converted to that data type through some path
 * Each data type relationship contains two information: target data type and conversion path
 * This interface can be used:
 * 		Build all operations for particular data type
 * 		Quickly find whether a data type is compatible with another data type 
 * 		Show whole picture for particular data type on UI
 */
public interface HAPDataTypePicture {

	HAPDataType getSourceDataType();

	HAPRelationship getRelationship(HAPDataTypeId dataTypeInfo);

	Set<? extends HAPRelationship> getRelationships();

}
