package com.nosliw.data;

import com.nosliw.common.serialization.HAPSerialiableImp;
import com.nosliw.common.utils.HAPConstant;

/**
 * Segment in path that indicate how to convert source data type to target data type
 * There are only two type of segment: parent and linked  
 */
public class HAPRelationshipPathSegment extends HAPSerialiableImp{

	private static HAPRelationshipPathSegment parent = new HAPRelationshipPathSegment(HAPConstant.DATATYPE_PATHSEGMENT_PARENT);
	private static HAPRelationshipPathSegment linked = new HAPRelationshipPathSegment(HAPConstant.DATATYPE_PATHSEGMENT_LINKED);
	
	private int m_segment;
	
	public HAPRelationshipPathSegment(int segType){
		this.m_segment = segType;
	}

	public static HAPRelationshipPathSegment buildPathSegment(int segType){
		HAPRelationshipPathSegment out = null;
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
	
	public static HAPRelationshipPathSegment buildPathSegmentForParent(){
		return parent;
	}
	
	public static HAPRelationshipPathSegment buildPathSegmentForLinked(){
		return linked;
	}
	
	public int getSegment(){
		return this.m_segment;
	}
	
	public boolean equals(Object segment){
		boolean out = false;
		if(segment instanceof HAPRelationshipPathSegment){
			out = (segment == this);
		}
		return out;
	}
	
	protected void buildObjectByLiterate(String literateValue){ this.m_segment = Integer.valueOf(literateValue);	}
	
	protected String buildLiterate(){  return this.m_segment+""; }

}
