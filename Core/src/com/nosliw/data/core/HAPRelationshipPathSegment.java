package com.nosliw.data.core;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;

/**
 * Segment in path that indicate how to convert source data type to target data type
 * There are only two type of segment: parent and linked  
 */
public class HAPRelationshipPathSegment extends HAPSerializableImp{

	private String m_type;
	private String m_id;
	
	public HAPRelationshipPathSegment(){}
	
	public HAPRelationshipPathSegment(HAPDataTypeId dataTypeId){
		this.m_type = HAPConstant.DATATYPE_PATHSEGMENT_PARENT;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE);
	}

	public HAPRelationshipPathSegment(HAPDataTypeVersion dataTypeVersion){
		this.m_type = HAPConstant.DATATYPE_PATHSEGMENT_LINKED;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(dataTypeVersion, HAPSerializationFormat.LITERATE);
	}

	public String getType(){		return this.m_type;	}
	public String getId(){  return this.m_id; }
	
	public boolean equals(Object segment){
		boolean out = false;
		if(segment instanceof HAPRelationshipPathSegment){
			out = (segment == this);
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] details = HAPNamingConversionUtility.parseDetails(literateValue);
		this.m_type = details[0];
		this.m_id = details[1];
		return true;
	}
	
	@Override
	protected String buildLiterate(){  
		return HAPNamingConversionUtility.cascadeDetail(m_type, m_id);
	}
}
