package com.nosliw.data.core.domain.entity.script.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.domain.entity.task.HAPRequirementTask;

@HAPEntityWithAttribute
public class HAPDefinitionTaskScript extends HAPInfoImpSimple{

	@HAPAttribute
	public static final String REQUIREMENT = "requirement";

	private List<HAPRequirementTask> m_requirement;
	
	public HAPDefinitionTaskScript() {
		this.m_requirement = new ArrayList<HAPRequirementTask>();
	}
	
	public List<HAPRequirementTask> getRequiredInteface(){		return this.m_requirement;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REQUIREMENT, HAPSerializeManager.getInstance().toStringValue(m_requirement, HAPSerializationFormat.JSON));
	}
}
