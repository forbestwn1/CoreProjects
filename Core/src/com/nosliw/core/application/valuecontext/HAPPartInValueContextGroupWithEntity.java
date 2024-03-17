package com.nosliw.core.application.valuecontext;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public class HAPPartInValueContextGroupWithEntity extends HAPPartInValueContext{

	private List<HAPPartInValueContext> m_children;
	
	public HAPPartInValueContextGroupWithEntity(HAPInfoPartInValueContext partInfo) {
		super(partInfo);
		this.m_children = new ArrayList<HAPPartInValueContext>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY;    }

	public List<HAPPartInValueContext> getChildren(){   return this.m_children;   }
	
	public String addChild(HAPPartInValueContext child) {
		if(child.isEmpty())  return null;
		child.getPartInfo().appendParentInfo(this.getPartInfo().getPriority());
		this.m_children.add(child);
		return child.getPartInfo().getName();
	}
	
	public HAPPartInValueContextGroupWithEntity cloneValueStructureComplexPartGroup() {
		HAPPartInValueContextGroupWithEntity out = new HAPPartInValueContextGroupWithEntity(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToPartValueContext(out);
		for(HAPPartInValueContext child : this.m_children) {
			out.addChild(child.cloneValueContextPart());
		}
		return out;
	}

	@Override
	public HAPPartInValueContext cloneValueContextPart() {   return this.cloneValueStructureComplexPartGroup();  }

	@Override
	public HAPPartInValueContext inheritValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates) {
		HAPPartInValueContextGroupWithEntity out = new HAPPartInValueContextGroupWithEntity(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToPartValueContext(out);
		for(HAPPartInValueContext child : this.m_children) {
			out.addChild(child.inheritValueContextPart(valueStructureDomain, mode, groupTypeCandidates));
		}
		return out;
	}
	
	@Override
	public boolean isEmpty() {	
		boolean out = true;
		for(HAPPartInValueContext child : this.m_children) {
			if(!child.isEmpty())  out = false;
		}
		return out;
	}
}
