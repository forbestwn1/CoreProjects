package com.nosliw.data;

import com.nosliw.common.utils.HAPConstant;

/**
 * Segment in path that indicate how to convert source data type to target data type
 * There are only two type of segment: parent and linked  
 */
public class HAPDataTypePathSegment {

	private static HAPDataTypePathSegment parent = new HAPDataTypePathSegment(HAPConstant.DATATYPE_PATHSEGMENT_PARENT);
	private static HAPDataTypePathSegment linked = new HAPDataTypePathSegment(HAPConstant.DATATYPE_PATHSEGMENT_LINKED);
	
	private int m_segment;
	
	public HAPDataTypePathSegment(int segType){
		this.m_segment = segType;
	}

	public static HAPDataTypePathSegment buildPathSegment(int segType){
		HAPDataTypePathSegment out = null;
		switch(segType){
		case HAPConstant.DATATYPE_PATHSEGMENT_PARENT:
			out = parent;
			break;
		case HAPConstant.DATATYPE_PATHSEGMENT_LINKED:
			out = linked;
			break;
		}
		return out;
	}
	
	public static HAPDataTypePathSegment buildPathSegmentForParent(){
		return parent;
	}
	
	public static HAPDataTypePathSegment buildPathSegmentForLinked(){
		return linked;
	}
	
	public int getSegment(){
		return this.m_segment;
	}
	
	public boolean equals(Object segment){
		boolean out = false;
		if(segment instanceof HAPDataTypePathSegment){
			out = (segment == this);
		}
		return out;
	}
}
