package com.nosliw.core.data;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializable;

/*
 * The interface the data type
 * For each data type, there are two part: basic information and operations
 * This interface is about basic information for data type, including:
 * 		description
 * 		info : DataTypeInfo of this data type
 * 		parent : the parent data type
 *      linkedVersion: the linked version of the same type
 * All data type may have two related data type: parent and linked data type
 * 		For one data type, we can build a data type picture in which have all the data types that that data type have connection to 
 * All other information for operation related with data type are all defined in operation part
 * For instance, how to build new data, converting between data type, validating the data, serialize and deserialize 
 */

@HAPEntityWithAttribute
public interface HAPDataType extends HAPSerializable{

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String PARENTSINFO = "parentsInfo";

	@HAPAttribute
	public static String LINKEDVERSION = "linkedVersion";
	
	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";
	
	/*
	 * get basic information for this data type (categary, type, description)
	 */
	HAPDataTypeId getName();

	HAPInfo getInfo();
	
	List<HAPDataTypeId> getParentsInfo();

	HAPDataTypeVersion getLinkedVersion();
	
	boolean getIsComplex();
	
}
