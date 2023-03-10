package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

public class HAPDBColumnInfo extends HAPStringableValueEntity{

	//talbe column name prefix
	public static final String COLUMN_PREFIX = "column_prefix";
	
	//table column, be default use the attribute name
	public static final String COLUMN = "column";

	//whether this column is primary key
	public static final String PRIMARYKEY = "primaryKey";
	
	//property name/path used to define the column from the root entity defining the table
	public static final String PROPERTY = "property";
	
	//data type/sub data type, used when do get/set during query. by default, use the getter return type
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

	public HAPDBColumnInfo(){
		this.updateAtomicChildStrValue(PRIMARYKEY, String.valueOf(false));
	}
	
	public String getProperty(){  return this.getAtomicAncestorValueString(PROPERTY);  }
	public void setProperty(String property){ this.updateAtomicChildStrValue(PROPERTY, property);  }
	
	public String getColumnName(){
		String prefix = this.getColumnNamePrefix();
		if(prefix==null)		return this.getAtomicAncestorValueString(COLUMN);
		else return prefix + "_" + this.getAtomicAncestorValueString(COLUMN);
	}
	public void setColumnName(String name){  this.updateAtomicChildStrValue(COLUMN, name);  } 
	public String getColumnNamePrefix(){ return this.getAtomicAncestorValueString(COLUMN_PREFIX);  }
	public void setColumnNamePrefix(String prefix){  this.updateAtomicChildStrValue(COLUMN_PREFIX, prefix);  }
	public String getDataType(){  return this.getAtomicAncestorValueString(DATATYPE);  }
	public String getSubDataType(){  return this.getAtomicAncestorValueString(SUBDATATYPE);  }

	public String getDefinition(){  return this.getAtomicAncestorValueString(DEFINITION);  }
	
	public String getGetter(){  return this.getAtomicAncestorValueString(GETTER);  }
	public String getGetterPath(){  return this.getAtomicAncestorValueString(GETTER_PATH);  }
	public void setGetterPath(String path){  this.updateAtomicChildStrValue(GETTER_PATH, path);  }
	
	public String getSetter(){  return this.getAtomicAncestorValueString(SETTER);  }
	public String getSetterPath(){  return this.getAtomicAncestorValueString(SETTER_PATH);  }
	public void setSetterPath(String path){  this.updateAtomicChildStrValue(SETTER_PATH, path);  }
	
	public boolean isPrimaryKey(){  return this.getAtomicAncestorValueBoolean(PRIMARYKEY);  }
	
}
