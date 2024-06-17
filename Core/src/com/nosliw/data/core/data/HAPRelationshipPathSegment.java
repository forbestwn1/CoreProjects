package com.nosliw.data.core.data;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

/**
 * Segment in path that indicate how to convert source data type to target data type
 * There are only two type of segment: parent and linked  
 */
public class HAPRelationshipPathSegment extends HAPSerializableImp{
	//type of segment (parent or link)
	private String m_type;
	//target data type Id
	private String m_id;
	
	public HAPRelationshipPathSegment(){} 
	
	public HAPRelationshipPathSegment(HAPDataTypeId dataTypeId){
		this.m_type = HAPConstantShared.DATATYPE_PATHSEGMENT_PARENT;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE);
	}

	public HAPRelationshipPathSegment(HAPDataTypeVersion dataTypeVersion){
		this.m_type = HAPConstantShared.DATATYPE_PATHSEGMENT_LINKED;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(dataTypeVersion, HAPSerializationFormat.LITERATE);
	}

	public String getType(){		return this.m_type;	}
	public String getId(){  return this.m_id; }
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPRelationshipPathSegment){
			HAPRelationshipPathSegment seg = (HAPRelationshipPathSegment)obj;
			if(!HAPUtilityBasic.isEquals(this.m_id, seg.m_id))  return false;
			if(!HAPUtilityBasic.isEquals(this.m_type, seg.m_type))  return false;
			out = true;
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] details = HAPUtilityNamingConversion.parseLevel2(literateValue);
		this.m_type = details[0];
		this.m_id = details[1];
		return true;
	}
	
	@Override
	protected String buildLiterate(){  
		return HAPUtilityNamingConversion.cascadeLevel2(m_type, m_id);
	}
}
