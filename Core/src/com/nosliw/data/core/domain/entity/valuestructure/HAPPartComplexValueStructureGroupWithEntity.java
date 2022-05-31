package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
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
		child.getPartInfo().appendParentInfo(this.getPartInfo().getPriority());
		this.m_children.add(child);
		return child.getPartInfo().getName();
	}
	
	public HAPPartComplexValueStructureGroupWithEntity cloneValueStructureComplexPartGroup() {
		HAPPartComplexValueStructureGroupWithEntity out = new HAPPartComplexValueStructureGroupWithEntity();
		this.cloneToEntityInfo(out);
		for(HAPPartComplexValueStructure child : this.m_children) {
			this.m_children.add(child.cloneComplexValueStructurePart());
		}
		return out;
	}

	@Override
	public HAPPartComplexValueStructure cloneComplexValueStructurePart() {   return this.cloneValueStructureComplexPartGroup();  }

	@Override
	public HAPPartComplexValueStructure cloneComplexValueStructurePart(HAPDomainValueStructure valueStructureDomain, String mode) {
		HAPPartComplexValueStructureGroupWithEntity out = new HAPPartComplexValueStructureGroupWithEntity();
		this.cloneToEntityInfo(out);
		for(HAPPartComplexValueStructure child : this.m_children) {
			this.m_children.add(child.cloneComplexValueStructurePart(valueStructureDomain, mode));
		}
		return out;
	}
}
