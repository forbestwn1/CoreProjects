package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPExtraInfoEntityInDomainExecutable extends HAPEntityInfoImp{

	public static final String DEFINITIONENTITYID = "definitionEntityId";
	
	private HAPIdEntityInDomain m_definitionEntityId;

	public HAPExtraInfoEntityInDomainExecutable() {	}

	public HAPExtraInfoEntityInDomainExecutable(HAPIdEntityInDomain definitionEntityId) {
		this.m_definitionEntityId = definitionEntityId;
	}

	public HAPIdEntityInDomain getEntityDefinitionId() {    return this.m_definitionEntityId;     }
	
	public HAPExtraInfoEntityInDomainExecutable cloneExtraInfo() {
		HAPExtraInfoEntityInDomainExecutable out = new HAPExtraInfoEntityInDomainExecutable();
		this.cloneToEntityInfo(out);
		out.m_definitionEntityId = this.m_definitionEntityId.cloneIdEntityInDomain();
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITIONENTITYID, this.m_definitionEntityId.toStringValue(HAPSerializationFormat.JSON));
	}
}
