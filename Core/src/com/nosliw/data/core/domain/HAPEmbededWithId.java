package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPEmbededWithId extends HAPEmbeded implements HAPExpandable{

	@HAPAttribute
	public static String EMBEDED = "embeded";

	@HAPAttribute
	public static String ENTITYID = "entityId";

	public HAPEmbededWithId(HAPIdEntityInDomain entityId) {
		super(entityId);
	}
	
	public HAPIdEntityInDomain getEntityId() {	return (HAPIdEntityInDomain)this.getEntity();	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {  this.setEntity(entityId);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYID, this.getEntityId().toStringValue(HAPSerializationFormat.LITERATE));
		if(this.getAdapter()!=null) {
			jsonMap.put(ADAPTER, this.getAdapter().toString());
		}
	}
	
	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EMBEDED, HAPUtilityDomain.getEntityExpandedJsonString(this.getEntityId(), entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
