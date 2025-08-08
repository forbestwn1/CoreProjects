package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.common.valuecontext22.HAPManualInfoPartInValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext22.HAPManualPartInValueContext;

public class HAPManualPartInValueContextGroupWithReference extends HAPManualPartInValueContext{

	private List<String> m_parentPartIds;
	
	public HAPManualPartInValueContextGroupWithReference(HAPManualInfoPartInValueContext partInfo) {
		super(partInfo);
		this.m_parentPartIds = new ArrayList<String>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHREFERENCE;    }

	
	public HAPManualPartInValueContextGroupWithReference cloneValueStructureComplexPartGroup() {
		HAPManualPartInValueContextGroupWithReference out = new HAPManualPartInValueContextGroupWithReference(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToEntityInfo(out);
		out.m_children.addAll(this.m_children);
		return out;
	}

	@Override
	public HAPManualPartInValueContext cloneValueContextPart() {   return this.cloneValueStructureComplexPartGroup();  }
}
