package com.nosliw.data.core.domain.container;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbededWithIdDefinition;

public class HAPElementContainerDefinitionWithId extends HAPElementContainerDefinition<HAPEmbededWithIdDefinition>{

	public HAPElementContainerDefinitionWithId(HAPEmbededWithIdDefinition embededWithId, String elementId) {
		super(embededWithId, elementId);
	}

	public HAPElementContainerDefinitionWithId() {	}

	@Override
	public HAPElementContainerDefinitionWithId cloneContainerElement() {
		HAPElementContainerDefinitionWithId out = new HAPElementContainerDefinitionWithId();
		this.cloneToInfoContainerElement(out);
		return out;
	}

	public HAPElementContainerDefinitionWithId cloneContainerElementInfoSet() {	return this.cloneContainerElement();	}
	
	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.getEmbededElementEntity().toExpandedJsonString(entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
