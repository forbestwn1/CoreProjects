package com.nosliw.data.core.complex.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public class HAPPartComplexValueStructureGroupWithEntity extends HAPPartComplexValueStructure{

	private List<HAPPartComplexValueStructure> m_children;
	
	public HAPPartComplexValueStructureGroupWithEntity() {}
	
	public HAPPartComplexValueStructureGroupWithEntity(HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_children = new ArrayList<HAPPartComplexValueStructure>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY;    }

	public List<HAPPartComplexValueStructure> getChildren(){   return this.m_children;   }
	
	public String addChild(HAPPartComplexValueStructure child) {
		child.getPartInfo().appendParentInfo(this.getPartInfo().getId(), this.getPartInfo().getPriority());
		this.m_children.add(child);
		return child.getPartInfo().getId();
	}
	
	public HAPPartComplexValueStructureGroupWithEntity cloneValueStructureComplexPartGroup() {
		HAPPartComplexValueStructureGroupWithEntity out = new HAPPartComplexValueStructureGroupWithEntity();
		this.cloneToEntityInfo(out);
		out.m_children.addAll(this.m_children);
		return out;
	}

	@Override
	public HAPPartComplexValueStructure cloneComplexValueStructurePart() {   return this.cloneValueStructureComplexPartGroup();  }
}
