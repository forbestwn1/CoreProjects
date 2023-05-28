package com.nosliw.data.core.domain.valuecontext;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public abstract class HAPExecutablePartValueContext extends HAPEntityInfoImp{
	
	private HAPInfoPartValueStructure m_partInfo;
	
	public HAPExecutablePartValueContext() {}
	
	public HAPExecutablePartValueContext(HAPInfoPartValueStructure partInfo) {
		this.m_partInfo = processPartInfo(partInfo);
	}

	public HAPInfoPartValueStructure getPartInfo() {    return this.m_partInfo;    }
	
	abstract public String getPartType();
	
	abstract public HAPExecutablePartValueContext cloneValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates);
	abstract public HAPExecutablePartValueContext cloneValueContextPart();
	
	abstract public boolean isEmpty();

	protected void cloneToPartValueContext(HAPExecutablePartValueContext part) {
		this.cloneToEntityInfo(part);
		part.m_partInfo = this.m_partInfo.cloneValueStructurePartInfo();
	}
	
	private HAPInfoPartValueStructure processPartInfo(HAPInfoPartValueStructure partInfo) {
		HAPInfoPartValueStructure out = partInfo;
		if(out==null)  out = HAPUtilityValueContext.createPartInfoDefault();  
		return out;
	}
}
