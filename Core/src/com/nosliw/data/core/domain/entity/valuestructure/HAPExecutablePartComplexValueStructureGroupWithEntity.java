package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public class HAPExecutablePartComplexValueStructureGroupWithEntity extends HAPExecutablePartComplexValueStructure{

	private List<HAPExecutablePartComplexValueStructure> m_children;
	
	public HAPExecutablePartComplexValueStructureGroupWithEntity() {
		this.m_children = new ArrayList<HAPExecutablePartComplexValueStructure>();
	}
	
	public HAPExecutablePartComplexValueStructureGroupWithEntity(HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_children = new ArrayList<HAPExecutablePartComplexValueStructure>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY;    }

	public List<HAPExecutablePartComplexValueStructure> getChildren(){   return this.m_children;   }
	
	public String addChild(HAPExecutablePartComplexValueStructure child) {
		if(child.isEmpty())  return null;
		child.getPartInfo().appendParentInfo(this.getPartInfo().getPriority());
		this.m_children.add(child);
		return child.getPartInfo().getName();
	}
	
	public HAPExecutablePartComplexValueStructureGroupWithEntity cloneValueStructureComplexPartGroup() {
		HAPExecutablePartComplexValueStructureGroupWithEntity out = new HAPExecutablePartComplexValueStructureGroupWithEntity();
		this.cloneToPartComplexValueStructure(out);
		for(HAPExecutablePartComplexValueStructure child : this.m_children) {
			out.addChild(child.cloneComplexValueStructurePart());
		}
		return out;
	}

	@Override
	public HAPExecutablePartComplexValueStructure cloneComplexValueStructurePart() {   return this.cloneValueStructureComplexPartGroup();  }

	@Override
	public HAPExecutablePartComplexValueStructure cloneComplexValueStructurePart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates) {
		HAPExecutablePartComplexValueStructureGroupWithEntity out = new HAPExecutablePartComplexValueStructureGroupWithEntity();
		this.cloneToPartComplexValueStructure(out);
		for(HAPExecutablePartComplexValueStructure child : this.m_children) {
			out.addChild(child.cloneComplexValueStructurePart(valueStructureDomain, mode, groupTypeCandidates));
		}
		return out;
	}
	
	@Override
	public boolean isEmpty() {	
		boolean out = true;
		for(HAPExecutablePartComplexValueStructure child : this.m_children) {
			if(!child.isEmpty())  out = false;
		}
		return out;
	}
}
