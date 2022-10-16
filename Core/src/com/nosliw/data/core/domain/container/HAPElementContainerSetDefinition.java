package com.nosliw.data.core.domain.container;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbededWithIdDefinition;
import com.nosliw.data.core.domain.HAPExpandable;

public class HAPElementContainerSetDefinition extends HAPElementContainerSet<HAPEmbededWithIdDefinition> implements HAPExpandable{

	public HAPElementContainerSetDefinition(HAPEmbededWithIdDefinition embededWithId, String elementId) {
		super(embededWithId, elementId);
	}

	public HAPElementContainerSetDefinition() {	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET;    }

	@Override
	public HAPElementContainerSetDefinition cloneContainerElementInfo() {
		HAPElementContainerSetDefinition out = new HAPElementContainerSetDefinition();
		this.cloneToInfoContainerElement(out);
		return out;
	}

	public HAPElementContainerSetDefinition cloneContainerElementInfoSet() {	return this.cloneContainerElementInfo();	}
	
	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.getEmbededElementEntity().toExpandedJsonString(entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
