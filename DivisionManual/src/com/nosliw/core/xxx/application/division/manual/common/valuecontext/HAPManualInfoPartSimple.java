package com.nosliw.core.xxx.application.division.manual.common.valuecontext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContextSimple;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public class HAPManualInfoPartSimple {

	public static final String PART = "part";
	
	private HAPManualPartInValueContextSimple m_simpleStructurePart;
	
	private List<Integer> m_priority;
	
	public HAPManualInfoPartSimple(HAPManualPartInValueContextSimple simpleStructurePart) {
		this.m_simpleStructurePart = simpleStructurePart;
	}

	public HAPManualPartInValueContextSimple getSimpleValueStructurePart() {	return this.m_simpleStructurePart;	}
	
	public List<Integer> getPriority(){   return this.m_priority;    }
	public void setPriority(List<Integer> priority) {     this.m_priority = priority;     }

	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(PART, this.m_simpleStructurePart.toExpandedString(valueStructureDomain));
		return HAPUtilityJson.buildMapJson(jsonMap);
	}
}
