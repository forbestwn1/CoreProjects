package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public abstract class HAPManualPartInValueContext extends HAPEntityInfoImp{
	
	public static final String PARTINFO = "partInfo";
	
	private HAPManualInfoPartInValueContext m_partInfo;
	
	public HAPManualPartInValueContext(HAPManualInfoPartInValueContext partInfo) {
		this.m_partInfo = processPartInfo(partInfo);
	}

	public HAPManualInfoPartInValueContext getPartInfo() {    return this.m_partInfo;    }
	
	abstract public String getPartType();
	
	abstract public HAPManualPartInValueContext inheritValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates);
	abstract public HAPManualPartInValueContext cloneValueContextPart();
	
	abstract public boolean isEmpty(HAPDomainValueStructure valueStructureDomain);

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
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PARTINFO, this.m_partInfo.toStringValue(HAPSerializationFormat.JSON));
	}

}
