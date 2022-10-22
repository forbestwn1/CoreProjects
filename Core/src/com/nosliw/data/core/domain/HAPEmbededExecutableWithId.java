package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

@HAPEntityWithAttribute
public class HAPEmbededExecutableWithId extends HAPEmbededExecutable{

	@HAPAttribute
	public static String ENTITYID = "entityId";

	@HAPAttribute
	public static String ENTITY = "entity";

	public HAPEmbededExecutableWithId() {}
	
	public HAPEmbededExecutableWithId(HAPIdEntityInDomain entityId, boolean isComplex) {
		super(entityId, entityId.getEntityType(), isComplex);
	}
	
	public HAPIdEntityInDomain getEntityId() {	return (HAPIdEntityInDomain)this.getValue();	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {  this.setValue(entityId);  }

	@Override
	public HAPEmbededExecutableWithId cloneEmbeded() {
		HAPEmbededExecutableWithId out = new HAPEmbededExecutableWithId();
		this.cloneToEmbeded(out);
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
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		return new ArrayList<HAPResourceDependency>();
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {	
		jsonMap.put(ENTITY, HAPUtilityDomain.getEntityExpandedJsonString(this.getEntityId(), entityDomain));
	}

}
