package com.nosliw.common.constant;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntityBasic;
import com.nosliw.common.strvalue.basic.HAPStringableValueList;

public class HAPConstantGroup extends HAPStringableValueEntityBasic{

	public final static String TYPE_CONSTANT = "constant"; 
	public final static String TYPE_ATTRIBUTE = "attribute"; 
	public final static String TYPE_CLASSATTR = "classAttr"; 
	
	public static String ENTITY_PROPERTY_TYPE = "type";
	public static String ENTITY_PROPERTY_FILEPATH = "filepath";
	public static String ENTITY_PROPERTY_CLASSNAME = "classname";
	public static String ENTITY_PROPERTY_PACKAGENAME = "packagename";
	public static String ENTITY_PROPERTY_DEFINITIONS = "definitions";

	public HAPConstantGroup(){}
	public HAPConstantGroup(String type){
		this.updateBasicChild(ENTITY_PROPERTY_TYPE, type);
	}

	public void addConstantInfo(HAPConstantInfo constantInfo){
		HAPStringableValueList list = (HAPStringableValueList)this.getChild(ENTITY_PROPERTY_DEFINITIONS);
		list.addChild(constantInfo);
	}
	
	public String getType(){ return this.getBasicAncestorValueString(ENTITY_PROPERTY_TYPE); }
	public String getFilePath(){ return this.getBasicAncestorValueString(ENTITY_PROPERTY_FILEPATH); }
	public String getClassName(){ return this.getBasicAncestorValueString(ENTITY_PROPERTY_CLASSNAME); }
	public String getPackageName(){ return this.getBasicAncestorValueString(ENTITY_PROPERTY_PACKAGENAME);  }
}
