package com.nosliw.core.application.division.manual.common.valuecontext;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public abstract class HAPManualPartInValueContext extends HAPEntityInfoImp{
	
	private HAPManualInfoPartInValueContext m_partInfo;
	
	public HAPManualPartInValueContext(HAPManualInfoPartInValueContext partInfo) {
		this.m_partInfo = processPartInfo(partInfo);
	}

	public HAPManualInfoPartInValueContext getPartInfo() {    return this.m_partInfo;    }
	
	abstract public String getPartType();
	
	abstract public HAPManualPartInValueContext inheritValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates);
	abstract public HAPManualPartInValueContext cloneValueContextPart();
	
	abstract public boolean isEmpty();

	public void cloneToPartValueContext(HAPManualPartInValueContext part) {
		this.cloneToEntityInfo(part);
	}
	
	private HAPManualInfoPartInValueContext processPartInfo(HAPManualInfoPartInValueContext partInfo) {
		HAPManualInfoPartInValueContext out = partInfo;
		if(out==null) {
			out = HAPManualUtilityValueContext.createPartInfoDefault();
		}  
		return out;
	}
}
