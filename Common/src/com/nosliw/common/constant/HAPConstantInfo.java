package com.nosliw.common.constant;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

public class HAPConstantInfo extends HAPStringableValueEntity{

	public static String ENTITY_PROPERTY_NAME = "name";
	public static String ENTITY_PROPERTY_TYPE = "type";
	public static String ENTITY_PROPERTY_VALUE = "value";
	public static String ENTITY_PROPERTY_SKIP = "skip";
	
	static public HAPConstantInfo build(String name, String value){
		HAPConstantInfo out = buildDefault(HAPConstantInfo.class);
		out.updateAtomicChildStrValue(ENTITY_PROPERTY_NAME, name);
		out.updateAtomicChildStrValue(ENTITY_PROPERTY_VALUE, value);
		return out;
	}
	
	
	public String getName(){ return this.getAtomicAncestorValueString(ENTITY_PROPERTY_NAME); }
	public String getType(){ return this.getAtomicAncestorValueString(ENTITY_PROPERTY_TYPE); }
	public String getValue(){ return this.getAtomicAncestorValueString(ENTITY_PROPERTY_VALUE); }
	public String getSkip(){ return this.getAtomicAncestorValueString(ENTITY_PROPERTY_SKIP); }
	
}
