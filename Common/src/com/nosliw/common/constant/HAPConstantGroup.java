package com.nosliw.common.constant;

import java.util.Iterator;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntityBasic;
import com.nosliw.common.strvalue.basic.HAPStringableValueList;

@EntityWithAttribute(baseName="CONSTANTGROUP")
public class HAPConstantGroup extends HAPStringableValueEntityBasic implements HAPEntityWithAttributeConstant{

	public final static String TYPE_CONSTANT = "constant"; 
	public final static String TYPE_ATTRIBUTE = "attribute"; 
	public final static String TYPE_CLASSATTR = "classAttr"; 
	
	public static String ENTITY_PROPERTY_CONSTANTGROUP_TYPE = "type";
	public static String ENTITY_PROPERTY_CONSTANTGROUP_FILEPATH = "filepath";
	public static String ENTITY_PROPERTY_CONSTANTGROUP_CLASSNAME = "classname";
	public static String ENTITY_PROPERTY_CONSTANTGROUP_PACKAGENAME = "packagename";
	public static String ENTITY_PROPERTY_CONSTANTGROUP_DEFINITIONS = "definitions";

	public HAPConstantGroup(){}
	public HAPConstantGroup(String type){
		this.updateBasicChild(ENTITY_PROPERTY_CONSTANTGROUP_TYPE, type);
	}

	public void addConstantInfo(HAPConstantInfo constantInfo){
		HAPStringableValueList list = this.getListChild(ENTITY_PROPERTY_CONSTANTGROUP_DEFINITIONS);
		list.addChild(constantInfo);
	}

	public Iterator iterateConstant(){
		HAPStringableValueList list = this.getListChild(ENTITY_PROPERTY_CONSTANTGROUP_DEFINITIONS);
		return list.iterate();
	}
	
	public String getType(){ return this.getBasicAncestorValueString(ENTITY_PROPERTY_CONSTANTGROUP_TYPE); }
	public String getFilePath(){ return this.getBasicAncestorValueString(ENTITY_PROPERTY_CONSTANTGROUP_FILEPATH); }
	public String getClassName(){ return this.getBasicAncestorValueString(ENTITY_PROPERTY_CONSTANTGROUP_CLASSNAME); }
	public String getPackageName(){ return this.getBasicAncestorValueString(ENTITY_PROPERTY_CONSTANTGROUP_PACKAGENAME);  }
}
