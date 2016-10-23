package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

/*
 * The interface for data type in Nosliw
 * in Nosliw, everything is data, 
 * user can implements this interface to defined their own type of data
 * for instance, text, url, image, boolean, event entity
 * when define a new data type, user need define:
 * 		1. how to create new data
 * 		2. the available operations for this type (create a data instance is also an operation)
 * 		3. the default data 
 * 		4. how to validate the value of data
 * 		5. serialize and deserialize 
 * 		6. for some data type, it has limited data, for instance, gender, country, week day, ect
 * 		7. version number. data type may change with new requirement, version number provide this information so that other part may adjust according to the value
 * 		8. parent data type. all data type can define parent data type from which data type can inherent operation from parent
 * varsion number has to be back-compatible
 * version number may be used in following situation:
 * 		1. before doing data operation, check if data meet the version number required by operation, if not, try to upgrade the data to reqired version
 * 		2. when ui is binded with data, it should set the data version when set the data
 */

@HAPEntityWithAttribute(baseName="DATATYPE")
public interface HAPDataType extends HAPSerializable{
	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String PARENTINFO = "parentInfo";

	@HAPAttribute
	public static String LINKEDVERSION = "linkedVersion";
	
	/*
	 * get basic information for this data type (categary, type, description)
	 */
	public HAPDataTypeInfo getDataTypeInfo();

	public String getDescription();
	
	
	/**
	 * get parent data type
	 * get older/newer data type
	 * all data type with different version form a chain
	 * through these methods, we know the next node on the chain, 
	 */
	public HAPDataTypeInfo getParentDataTypeInfo();
	public HAPDataTypeVersion getLinkedVersion();
}
