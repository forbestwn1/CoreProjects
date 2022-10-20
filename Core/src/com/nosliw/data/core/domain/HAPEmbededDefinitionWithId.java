package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPEmbededDefinitionWithId extends HAPEmbededDefinition{

	@HAPAttribute
	public static String ENTITYID = "entityId";

	@HAPAttribute
	public static String ENTITY = "entity";
	
	public HAPEmbededDefinitionWithId() {}
	
	public HAPEmbededDefinitionWithId(HAPIdEntityInDomain entityId) {
		super(entityId);
	}
	
	public HAPIdEntityInDomain getEntityId() {	return (HAPIdEntityInDomain)this.getValue();	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {  this.setValue(entityId);  }

	@Override
	public HAPEmbeded cloneEmbeded() {
		HAPEmbededDefinitionWithId out = new HAPEmbededDefinitionWithId();
		out.setEntityId(this.getEntityId().cloneIdEntityInDomain());
		out.setAdapter(this.getAdapter());
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYID, this.getEntityId().toStringValue(HAPSerializationFormat.LITERATE));
		if(this.getAdapter()!=null) {
			jsonMap.put(ADAPTER, this.getAdapter().toString());
		}
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {	
		jsonMap.put(ENTITY, HAPUtilityDomain.getEntityExpandedJsonString(this.getEntityId(), entityDomain));
	}
}
