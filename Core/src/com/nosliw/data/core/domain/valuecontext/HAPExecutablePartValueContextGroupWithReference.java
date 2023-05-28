package com.nosliw.data.core.domain.valuecontext;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecutablePartValueContextGroupWithReference extends HAPExecutablePartValueContext{

	private List<String> m_parentPartIds;
	
	public HAPExecutablePartValueContextGroupWithReference() {}
	
	public HAPExecutablePartValueContextGroupWithReference(HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_parentPartIds = new ArrayList<String>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHREFERENCE;    }

	
	public HAPExecutablePartValueContextGroupWithReference cloneValueStructureComplexPartGroup() {
		HAPExecutablePartValueContextGroupWithReference out = new HAPExecutablePartValueContextGroupWithReference();
		this.cloneToEntityInfo(out);
		out.m_children.addAll(this.m_children);
		return out;
	}

	@Override
	public HAPExecutablePartValueContext cloneValueContextPart() {   return this.cloneValueStructureComplexPartGroup();  }
}
