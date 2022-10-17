package com.nosliw.data.core.domain.container;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;

public abstract class HAPContainerEntityDefinition<T extends HAPElementContainerDefinition> extends HAPContainerEntity<T> implements HAPExpandable{

	public HAPContainerEntityDefinition() {	}

	public HAPContainerEntityDefinition(String eleType) {
		super(eleType);
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		
		List<String> eleArray = new ArrayList<String>();
		for(HAPElementContainerDefinition ele : this.getAllElementsInfo()) {
			eleArray.add(ele.toExpandedJsonString(entityDefDomain));
		}
		jsonMap.put(ELEMENT, HAPJsonUtility.buildArrayJson(eleArray.toArray(new String[0])));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
