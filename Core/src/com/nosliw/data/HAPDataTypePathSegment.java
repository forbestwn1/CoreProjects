package com.nosliw.data;

import com.nosliw.common.utils.HAPConstant;

public class HAPDataTypePathSegment {

	int m_segment;
	
	public HAPDataTypePathSegment(int segType){
		this.m_segment = segType;
	}

	public static HAPDataTypePathSegment buildPathSegment(int segType){
		return new HAPDataTypePathSegment(segType);
	}
	
	public static HAPDataTypePathSegment buildPathSegmentForParent(){
		return new HAPDataTypePathSegment(HAPConstant.DATATYPE_PATHSEGMENT_PARENT);
	}
	
	public static HAPDataTypePathSegment buildPathSegmentForLinked(){
		return new HAPDataTypePathSegment(HAPConstant.DATATYPE_PATHSEGMENT_LINKED);
	}
	
	public int getSegment(){
		return this.m_segment;
	}
}
