package com.nosliw.core.application.valuecontext;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPPartInValueContextGroupWithReference extends HAPPartInValueContext{

	private List<String> m_parentPartIds;
	
	public HAPPartInValueContextGroupWithReference(HAPInfoPartInValueContext partInfo) {
		super(partInfo);
		this.m_parentPartIds = new ArrayList<String>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHREFERENCE;    }

	
	public HAPPartInValueContextGroupWithReference cloneValueStructureComplexPartGroup() {
		HAPPartInValueContextGroupWithReference out = new HAPPartInValueContextGroupWithReference(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToEntityInfo(out);
		out.m_children.addAll(this.m_children);
		return out;
	}

	@Override
	public HAPPartInValueContext cloneValueContextPart() {   return this.cloneValueStructureComplexPartGroup();  }
}
