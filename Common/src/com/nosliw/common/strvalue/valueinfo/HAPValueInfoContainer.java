package com.nosliw.common.strvalue.valueinfo;

public abstract class HAPValueInfoContainer extends HAPValueInfoComplex{

	public static final String ENTITY_PROPERTY_CHILD = "child";
	
	public static final String ENTITY_PROPERTY_ELEMENTTAG = "elementTag";

	public HAPValueInfo getChildValueInfo(){
		HAPValueInfo childInfo = (HAPValueInfo)this.getAncestorByPath(HAPValueInfoMap.ENTITY_PROPERTY_CHILD);
		return childInfo;
	}

	public void setChildValueInfo(HAPValueInfo valueInfo){
		this.updateChild(ENTITY_PROPERTY_CHILD, valueInfo);
	}
	
}
