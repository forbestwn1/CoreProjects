package com.nosliw.data.core.script.context.dataassociation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

@HAPEntityWithAttribute
public class HAPExecutableGroupDataAssociation extends HAPSerializableImp implements HAPExecutable{

	@HAPAttribute
	public static final String ELEMENT = "element";

	private Map<String, HAPExecutableDataAssociation> m_dataAssociations;
	
	public HAPExecutableGroupDataAssociation() {
		this.m_dataAssociations = new LinkedHashMap<String, HAPExecutableDataAssociation>();
	} 
	
	public HAPExecutableDataAssociation getDataAssociation(String name) {  return this.m_dataAssociations.get(name);   }
	public HAPExecutableDataAssociation getDefaultDataAssociation() {  return this.m_dataAssociations.get(this.getDefaultName());   }
	
	public Map<String, HAPExecutableDataAssociation> getDataAssociations(){  return this.m_dataAssociations;   }

	public void addDataAssociation(String name, HAPExecutableDataAssociation dataAssociation) {   this.m_dataAssociations.put(name, dataAssociation);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> jsonEleMap = new LinkedHashMap<String, String>();
		for(String name : this.m_dataAssociations.keySet()) {
			jsonEleMap.put(name, this.m_dataAssociations.get(name).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ELEMENT, HAPJsonUtility.buildMapJson(jsonEleMap)); 
	}
	
	private String getDefaultName() {  return HAPConstantShared.GLOBAL_VALUE_DEFAULT;   }

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);

		Map<String, String> jsonEleMap = new LinkedHashMap<String, String>();
		for(String name : this.m_dataAssociations.keySet()) {
			jsonEleMap.put(name, this.m_dataAssociations.get(name).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ELEMENT, HAPJsonUtility.buildMapJson(jsonEleMap)); 
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(String name : this.m_dataAssociations.keySet()) {
			out.addAll(this.m_dataAssociations.get(name).getResourceDependency(runtimeInfo, resourceManager));
		}
		return out;
	}
}
