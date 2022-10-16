package com.nosliw.data.core.domain.container;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbededWithId;
import com.nosliw.data.core.domain.HAPExpandable;

public class HAPInfoContainerElementListDefinition extends HAPInfoContainerElementList<HAPEmbededWithId> implements HAPExpandable{

	public HAPInfoContainerElementListDefinition(HAPEmbededWithId embededWithId) {
		super(embededWithId);
		this.setIndex(-1);
	}

	public HAPInfoContainerElementListDefinition() {
		this.setIndex(-1);
	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST;    }

	@Override
	public String getElementId() {
		return this.getEmbededElementEntity().getEntityId().toString();
	} 
	
	public HAPInfoContainerElementListDefinition cloneContainerElementInfoList() {	return this.cloneContainerElementInfo();	}

	@Override
	public HAPInfoContainerElementListDefinition cloneContainerElementInfo() {
		HAPInfoContainerElementListDefinition out = new HAPInfoContainerElementListDefinition();
		this.cloneToInfoContainerElement(out);
		return out;
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.getEmbededElementEntity().toExpandedJsonString(entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
