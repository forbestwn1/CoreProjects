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
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

@HAPEntityWithAttribute
public class HAPEmbededWithIdExecutable extends HAPEmbeded implements HAPExecutable{

	@HAPAttribute
	public static String EMBEDED = "embeded";

	@HAPAttribute
	public static String ENTITYID = "entityId";

	public HAPEmbededWithIdExecutable() {}
	
	public HAPEmbededWithIdExecutable(HAPIdEntityInDomain entityId) {
		super(entityId);
	}
	
	public HAPIdEntityInDomain getEntityId() {	return (HAPIdEntityInDomain)this.getEntity();	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {  this.setEntity(entityId);  }

	@Override
	public HAPEmbeded cloneEmbeded() {
		HAPEmbededWithIdExecutable out = new HAPEmbededWithIdExecutable();
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
}
