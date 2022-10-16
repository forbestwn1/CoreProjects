package com.nosliw.data.core.domain.container;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbededWithId;
import com.nosliw.data.core.domain.HAPExpandable;

public class HAPInfoContainerElementSetDefinition extends HAPInfoContainerElementSet<HAPEmbededWithId> implements HAPExpandable{

	public HAPInfoContainerElementSetDefinition(HAPEmbededWithId embededWithId) {
		super(embededWithId);
	}

	public HAPInfoContainerElementSetDefinition() {	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET;    }

	@Override
	public String getElementId() {
		return this.getEmbededElementEntity().getEntityId().toString();
	} 

	@Override
	public HAPInfoContainerElementSetDefinition cloneContainerElementInfo() {
		HAPInfoContainerElementSetDefinition out = new HAPInfoContainerElementSetDefinition();
		this.cloneToInfoContainerElement(out);
		return out;
	}

	public HAPInfoContainerElementSetDefinition cloneContainerElementInfoSet() {	return this.cloneContainerElementInfo();	}
	
	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.getEmbededElementEntity().toExpandedJsonString(entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
