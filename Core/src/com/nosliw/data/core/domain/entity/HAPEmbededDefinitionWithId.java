package com.nosliw.data.core.domain.entity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;

@HAPEntityWithAttribute
public class HAPEmbededDefinitionWithId extends HAPEmbededDefinition{

	@HAPAttribute
	public static String ENTITYID = "entityId";

	@HAPAttribute
	public static String ENTITY = "entity";
	
	public HAPEmbededDefinitionWithId() {}
	
	public HAPEmbededDefinitionWithId(HAPIdEntityInDomain entityId, boolean isComplex) {
		super(entityId, entityId.getEntityType(), isComplex);
	}
	
	public HAPIdEntityInDomain getEntityId() {	return (HAPIdEntityInDomain)this.getValue();	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {  this.setValue(entityId);  }

	@Override
	public HAPEmbededDefinitionWithId cloneEmbeded() {
		HAPEmbededDefinitionWithId out = new HAPEmbededDefinitionWithId();
		this.cloneToEmbeded(out);
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITYID, this.getEntityId().toStringValue(HAPSerializationFormat.LITERATE));
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {	
		jsonMap.put(ENTITY, HAPUtilityDomain.getEntityExpandedJsonString(this.getEntityId(), entityDomain));
	}
}
