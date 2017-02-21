package com.nosliw.data.datatype.importer;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.HAPRelationshipPath;
import com.nosliw.data.HAPRelationshipPathSegment;

public class HAPRelationshipPathImp extends HAPRelationshipPath implements HAPSerializable {

	@Override
	public String toStringValue(HAPSerializationFormat format) {
		return null;
	}

	@Override
	public void buildObject(Object value, HAPSerializationFormat format) {
		
	} 

	public HAPRelationshipPathImp clone(){
		HAPRelationshipPathImp out = new HAPRelationshipPathImp();
		out.m_segments.addAll(this.m_segments);
		return out;
	}

	public void insert(HAPRelationshipPathSegment segment){
		this.m_segments.add(0, segment);
	}
	
}
