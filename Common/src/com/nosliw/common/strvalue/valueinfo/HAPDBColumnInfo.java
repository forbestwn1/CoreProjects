package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

public class HAPDBColumnInfo extends HAPStringableValueEntity{

	//table column, be default use the attribute name
	public static final String COLUMN = "column";

	//data type, used when do get/set during query. by default, use the getter return type
	public static final String TYPE = "type";

	//sql column definition
	public static final String DEFINITION = "definition";
	
	//getter method for this column. by default, use getPropertyName
	public static final String GETTER = "getter";

	//setter method for this column, by default, use setPropertyName, if its value is "no", no setter method  
	public static final String SETTER = "setter";

	
	public static final String ATTRPATH = "attrPath";
	
}
