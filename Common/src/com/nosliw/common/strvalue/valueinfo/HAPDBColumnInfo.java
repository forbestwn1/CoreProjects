package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

public class HAPDBColumnInfo extends HAPStringableValueEntity{

	//table column, be default use the attribute name
	public static final String COLUMN = "column";

	//data type, used when do get/set during query. by default, use the getter return type
	public static final String DATATYPE = "dataType";

	public static final String SUBDATATYPE = "subDataType";
	
	//sql column definition
	public static final String DEFINITION = "definition";
	
	//getter method for this column. by default, use getColumnName, 
	public static final String GETTER = "getter";

	//the path for the getter method
	public static final String GETTER_PATH = "getterPath";
	
	//setter method for this column, by default, use setPropertyName, if its value is "no", no setter method
	public static final String SETTER = "setter";

	//the path for the setter method
	public static final String SETTER_PATH = "setterPath";
	
	public String getColumnName(){  return this.getAtomicAncestorValueString(COLUMN);  }
	public String getDataType(){  return this.getAtomicAncestorValueString(DATATYPE);  }
	public String getSubDataType(){  return this.getAtomicAncestorValueString(SUBDATATYPE);  }

	public String getDefinition(){  return this.getAtomicAncestorValueString(DEFINITION);  }
	
	public String getGetter(){  return this.getAtomicAncestorValueString(GETTER);  }
	public String getGetterPath(){  return this.getAtomicAncestorValueString(GETTER_PATH);  }
	
	public String getSetter(){  return this.getAtomicAncestorValueString(SETTER);  }
	public String getSetterPath(){  return this.getAtomicAncestorValueString(SETTER_PATH);  }
	
}
