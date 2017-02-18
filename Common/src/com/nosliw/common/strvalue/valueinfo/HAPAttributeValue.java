package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

/**
 * Value Info attirbute and value 
 */
public class HAPAttributeValue extends HAPStringableValueEntity{

	//attribute name for value info
	public static final String ATTRIBUTE = "attribute";

	//attribute value for value info
	public static final String VALUE = "value";

	public HAPAttributeValue(String attr, String value){
		this.updateAtomicChildStrValue(ATTRIBUTE, attr);
		this.updateAtomicChildStrValue(VALUE, value);
	}
	
	public String getAttribute(){		return this.getAtomicAncestorValueString(ATTRIBUTE);	}

	public String getValue(){  return this.getAtomicAncestorValueString(VALUE); }
}
