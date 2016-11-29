package com.nosliw.data.datatype.importer;

public class HAPDataTypePathSegment {

	String m_pathType;
	
	Object m_info;

	public HAPDataTypePathSegment(String pathType){
		this.m_pathType = pathType;
	}
	
	public static HAPDataTypePathSegment buildPathSegmentForParent(){
		return new HAPDataTypePathSegment("parent");
	}
	
	public static HAPDataTypePathSegment buildPathSegmentForLinked(){
		return new HAPDataTypePathSegment("linked");
	}
	
	public String getPathSegmentType(){
		return this.m_pathType;
	}
}
