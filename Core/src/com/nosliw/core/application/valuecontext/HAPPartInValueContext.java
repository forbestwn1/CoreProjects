package com.nosliw.core.application.valuecontext;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public abstract class HAPPartInValueContext extends HAPEntityInfoImp{
	
	private HAPInfoPartInValueContext m_partInfo;
	
	public HAPPartInValueContext(HAPInfoPartInValueContext partInfo) {
		this.m_partInfo = processPartInfo(partInfo);
	}

	public HAPInfoPartInValueContext getPartInfo() {    return this.m_partInfo;    }
	
	abstract public String getPartType();
	
	abstract public HAPPartInValueContext inheritValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates);
	abstract public HAPPartInValueContext cloneValueContextPart();
	
	abstract public boolean isEmpty();

	protected void cloneToPartValueContext(HAPPartInValueContext part) {
		this.cloneToEntityInfo(part);
	}
	
	private HAPInfoPartInValueContext processPartInfo(HAPInfoPartInValueContext partInfo) {
		HAPInfoPartInValueContext out = partInfo;
		if(out==null)  out = HAPUtilityValueContext.createPartInfoDefault();  
		return out;
	}
}
