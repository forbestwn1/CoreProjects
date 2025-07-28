package com.nosliw.data.core.domain.valuecontext;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public class HAPExecutablePartValueContextGroupWithEntity extends HAPExecutablePartValueContext{

	private List<HAPExecutablePartValueContext> m_children;
	
	public HAPExecutablePartValueContextGroupWithEntity(HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_children = new ArrayList<HAPExecutablePartValueContext>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY;    }

	public List<HAPExecutablePartValueContext> getChildren(){   return this.m_children;   }
	
	public String addChild(HAPExecutablePartValueContext child) {
		if(child.isEmpty())  return null;
		child.getPartInfo().appendParentInfo(this.getPartInfo().getPriority());
		this.m_children.add(child);
		return child.getPartInfo().getName();
	}
	
	public HAPExecutablePartValueContextGroupWithEntity cloneValueStructureComplexPartGroup() {
		HAPExecutablePartValueContextGroupWithEntity out = new HAPExecutablePartValueContextGroupWithEntity(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToPartValueContext(out);
		for(HAPExecutablePartValueContext child : this.m_children) {
			out.addChild(child.cloneValueContextPart());
		}
		return out;
	}

	@Override
	public HAPExecutablePartValueContext cloneValueContextPart() {   return this.cloneValueStructureComplexPartGroup();  }

	@Override
	public HAPExecutablePartValueContext inheritValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates) {
		HAPExecutablePartValueContextGroupWithEntity out = new HAPExecutablePartValueContextGroupWithEntity(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToPartValueContext(out);
		for(HAPExecutablePartValueContext child : this.m_children) {
			out.addChild(child.inheritValueContextPart(valueStructureDomain, mode, groupTypeCandidates));
		}
		return out;
	}
	
	@Override
	public boolean isEmpty() {	
		boolean out = true;
		for(HAPExecutablePartValueContext child : this.m_children) {
			if(!child.isEmpty())  out = false;
		}
		return out;
	}
}
