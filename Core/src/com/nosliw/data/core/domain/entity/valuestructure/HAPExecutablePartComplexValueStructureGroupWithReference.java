package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public class HAPExecutablePartComplexValueStructureGroupWithReference extends HAPExecutablePartComplexValueStructure{

	private List<String> m_parentPartIds;
	
	public HAPExecutablePartComplexValueStructureGroupWithReference() {}
	
	public HAPExecutablePartComplexValueStructureGroupWithReference(HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_parentPartIds = new ArrayList<String>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHREFERENCE;    }

	
	public HAPExecutablePartComplexValueStructureGroupWithReference cloneValueStructureComplexPartGroup() {
		HAPExecutablePartComplexValueStructureGroupWithReference out = new HAPExecutablePartComplexValueStructureGroupWithReference();
		this.cloneToEntityInfo(out);
		out.m_children.addAll(this.m_children);
		return out;
	}

	@Override
	public HAPExecutablePartComplexValueStructure cloneComplexValueStructurePart() {   return this.cloneValueStructureComplexPartGroup();  }
}
