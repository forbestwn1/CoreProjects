package com.nosliw.data.core.domain.entity.valuestructure;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public abstract class HAPPartComplexValueStructure extends HAPEntityInfoImp{
	

	private HAPInfoPartValueStructure m_partInfo;
	
	public HAPPartComplexValueStructure() {}
	
	public HAPPartComplexValueStructure(HAPInfoPartValueStructure partInfo) {
		this.m_partInfo = processPartInfo(partInfo);
	}

	public HAPInfoPartValueStructure getPartInfo() {    return this.m_partInfo;    }
	
	abstract public String getPartType();
	
	abstract public HAPPartComplexValueStructure cloneComplexValueStructurePart(HAPDomainValueStructure valueStructureDomain, String mode);
	
	private HAPInfoPartValueStructure processPartInfo(HAPInfoPartValueStructure partInfo) {
		HAPInfoPartValueStructure out = partInfo;
		if(out==null)  out = HAPUtilityComplexValueStructure.createPartInfoDefault();  
		return out;
	}
}
