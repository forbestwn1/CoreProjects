package com.nosliw.data.core.domain.container;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbededWithIdDefinition;
import com.nosliw.data.core.domain.HAPExpandable;

public class HAPElementContainerListDefinition extends HAPElementContainerList<HAPEmbededWithIdDefinition> implements HAPExpandable{

	public HAPElementContainerListDefinition(HAPEmbededWithIdDefinition embededWithId, String elementId) {
		super(embededWithId, elementId);
		this.setIndex(-1);
	}

	public HAPElementContainerListDefinition() {
		this.setIndex(-1);
	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST;    }

	@Override
	public HAPElementContainerListDefinition cloneContainerElementInfo() {
		HAPElementContainerListDefinition out = new HAPElementContainerListDefinition();
		this.cloneToInfoContainerElement(out);
		return out;
	}

	public HAPElementContainerListDefinition cloneContainerElementInfoList() {	return this.cloneContainerElementInfo();	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.getEmbededElementEntity().toExpandedJsonString(entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
