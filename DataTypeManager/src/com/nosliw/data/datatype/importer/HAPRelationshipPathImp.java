package com.nosliw.data.datatype.importer;

import java.util.List;

import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPRelationshipPath;
import com.nosliw.data.core.HAPRelationshipPathSegment;

public class HAPRelationshipPathImp extends HAPRelationshipPath implements HAPSerializable {

	@Override
	public String toStringValue(HAPSerializationFormat format) {
		return HAPLiterateManager.getInstance().valueToString(this.m_segments);
	}

	@Override
	public void buildObject(Object value, HAPSerializationFormat format) {
		this.m_segments = (List<HAPRelationshipPathSegment>)HAPLiterateManager.getInstance().stringToValue((String)value, HAPConstant.STRINGABLE_ATOMICVALUETYPE_ARRAY, "com.nosliw.data.HAPRelationshipPathSegment");
	} 

	public HAPRelationshipPathImp clone(){
		HAPRelationshipPathImp out = new HAPRelationshipPathImp();
		out.m_segments.addAll(this.m_segments);
		return out;
	}

}
