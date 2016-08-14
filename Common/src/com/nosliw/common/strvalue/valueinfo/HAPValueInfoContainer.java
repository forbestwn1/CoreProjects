package com.nosliw.common.strvalue.valueinfo;

public abstract class HAPValueInfoContainer extends HAPValueInfoComplex{

	public static final String ATTR_CHILD = "child";
	
	public static final String ATTR_ELEMENTTAG = "elementTag";

	public HAPValueInfo getChildValueInfo(){
		HAPValueInfo childInfo = (HAPValueInfo)this.getAncestorByPath(HAPValueInfoMap.ATTR_CHILD);
		return childInfo;
	}
	
}
