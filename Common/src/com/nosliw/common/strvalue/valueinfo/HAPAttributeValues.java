package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstant;

/**
 *  Describe attribute values for child value info 
 */
public class HAPAttributeValues extends HAPStringableValueEntity{

	//the path to the child value info
	public static final String PATH = "path";
	
	//store a list of HAPAttributeValue (attribute -- value pair)
	public static final String ATTRIBUTES = "attributes";

	public HAPAttributeValues(String path){
		this.updateAtomicChildStrValue(PATH, path);
		this.updateComplexChild(ATTRIBUTES, HAPConstant.STRINGABLE_VALUESTRUCTURE_LIST);
	}
	
	public void addAttributeValue(HAPAttributeValue attrValue){
		this.getListAncestorByPath(ATTRIBUTES).addChild(attrValue);
	}

	public String getPath(){
		return this.getAtomicAncestorValueString(PATH);
	}
	
	public HAPStringableValueList<HAPAttributeValue> getAttributes(){
		return this.getListAncestorByPath(ATTRIBUTES);
	}
	
}
