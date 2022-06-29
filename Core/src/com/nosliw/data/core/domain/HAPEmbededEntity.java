package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPEmbededEntity extends HAPSerializableImp{

	@HAPAttribute
	public static String ENTITYID = "entityId";

	@HAPAttribute
	public static String EMBEDED = "embeded";

	@HAPAttribute
	public static String ADAPTER = "adapter";

	private HAPIdEntityInDomain m_entityId;
	
	private Object m_adapter;
	
	public Object getAdapter() {
		return m_adapter;
	}

	public HAPEmbededEntity(HAPIdEntityInDomain entityId) {
		this.m_entityId = entityId;
	}
	
	public void setAdapter(Object adapter) {
		this.m_adapter = adapter;
	}

	public HAPIdEntityInDomain getEntityId() {
		return this.m_entityId;
	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {
		this.m_entityId = entityId;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYID, this.m_entityId.toStringValue(HAPSerializationFormat.LITERATE));
		if(this.m_adapter!=null) {
			jsonMap.put(ADAPTER, this.m_adapter.toString());
		}
	}
	
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EMBEDED, HAPUtilityDomain.getEntityExpandedJsonString(this.m_entityId, entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);

	}
}
