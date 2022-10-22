package com.nosliw.data.core.domain;

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
public class HAPEmbededExecutableWithEntity extends HAPEmbededExecutable{

	@HAPAttribute
	public static String ENTITY = "entity";

	public HAPEmbededExecutableWithEntity() {}
	
	public HAPEmbededExecutableWithEntity(HAPExecutable executable, String entityType, boolean isComplex) {
		super(executable, entityType, isComplex);
	}
	
	public HAPExecutable getEntity() {	return (HAPExecutable)this.getValue();	}
	
	public void setEntity(HAPExecutable executable) {  this.setEntity(executable);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITY, this.getEntity().toStringValue(HAPSerializationFormat.LITERATE));
		if(this.getAdapter()!=null) {
			jsonMap.put(ADAPTER, this.getAdapter().toString());
		}
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.getEntity().toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {	
		if(this.getValue() instanceof HAPExpandable)   jsonMap.put(ENTITY, ((HAPExpandable) this.getValue()).toExpandedJsonString(entityDomain));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		return this.getEntity().getResourceDependency(runtimeInfo, resourceManager);
	}

	@Override
	public HAPEmbededExecutableWithEntity cloneEmbeded() {
		HAPEmbededExecutableWithEntity out = new HAPEmbededExecutableWithEntity();
		this.cloneToEmbeded(out);
		out.setAdapter(this.getAdapter());
		out.setValue(this.getValue());
		return out;
	}
}
