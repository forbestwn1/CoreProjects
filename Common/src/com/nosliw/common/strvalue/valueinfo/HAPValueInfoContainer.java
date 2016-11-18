package com.nosliw.common.strvalue.valueinfo;

public abstract class HAPValueInfoContainer extends HAPValueInfoComplex{

	public static final String CHILD = "child";
	
	public static final String ELEMENTTAG = "elementTag";

	public HAPValueInfo getChildValueInfo(){
		HAPValueInfo childInfo = (HAPValueInfo)this.getAncestorByPath(HAPValueInfoMap.CHILD);
		return childInfo;
	}

	public void setChildValueInfo(HAPValueInfo valueInfo){
		this.updateChild(CHILD, valueInfo);
	}
	
}
