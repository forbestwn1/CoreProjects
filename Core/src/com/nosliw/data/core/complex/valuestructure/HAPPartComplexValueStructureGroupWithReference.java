package com.nosliw.data.core.complex.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public class HAPPartComplexValueStructureGroupWithReference extends HAPPartComplexValueStructure{

	private List<String> m_parentPartIds;
	
	public HAPPartComplexValueStructureGroupWithReference() {}
	
	public HAPPartComplexValueStructureGroupWithReference(HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_parentPartIds = new ArrayList<String>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHREFERENCE;    }

	
	public HAPPartComplexValueStructureGroupWithReference cloneValueStructureComplexPartGroup() {
		HAPPartComplexValueStructureGroupWithReference out = new HAPPartComplexValueStructureGroupWithReference();
		this.cloneToEntityInfo(out);
		out.m_children.addAll(this.m_children);
		return out;
	}

	@Override
	public HAPPartComplexValueStructure cloneComplexValueStructurePart() {   return this.cloneValueStructureComplexPartGroup();  }
}
