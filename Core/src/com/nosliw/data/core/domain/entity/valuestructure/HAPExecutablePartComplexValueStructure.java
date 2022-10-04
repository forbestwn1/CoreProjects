package com.nosliw.data.core.domain.entity.valuestructure;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public abstract class HAPExecutablePartComplexValueStructure extends HAPEntityInfoImp{
	
	private HAPInfoPartValueStructure m_partInfo;
	
	public HAPExecutablePartComplexValueStructure() {}
	
	public HAPExecutablePartComplexValueStructure(HAPInfoPartValueStructure partInfo) {
		this.m_partInfo = processPartInfo(partInfo);
	}

	public HAPInfoPartValueStructure getPartInfo() {    return this.m_partInfo;    }
	
	abstract public String getPartType();
	
	abstract public HAPExecutablePartComplexValueStructure cloneComplexValueStructurePart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates);
	abstract public HAPExecutablePartComplexValueStructure cloneComplexValueStructurePart();
	
	abstract public boolean isEmpty();

	protected void cloneToPartComplexValueStructure(HAPExecutablePartComplexValueStructure part) {
		this.cloneToEntityInfo(part);
		part.m_partInfo = this.m_partInfo.cloneValueStructurePartInfo();
	}
	
	private HAPInfoPartValueStructure processPartInfo(HAPInfoPartValueStructure partInfo) {
		HAPInfoPartValueStructure out = partInfo;
		if(out==null)  out = HAPUtilityComplexValueStructure.createPartInfoDefault();  
		return out;
	}
}
