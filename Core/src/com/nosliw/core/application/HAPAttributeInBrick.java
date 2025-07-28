package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPWithResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPAttributeInBrick extends HAPEntityInfoImp implements HAPWithResourceDependency{

	@HAPAttribute
	public static final String VALUEWRAPPER = "valueWrapper";

	@HAPAttribute
	public static final String ADAPTER = "adapter";

	private HAPWrapperValue m_valueWrapper;
	
	private List<HAPAdapter> m_adapter;
	
	public HAPAttributeInBrick() {
		this.m_adapter = new ArrayList<HAPAdapter>();
	}
	
	public HAPAttributeInBrick(String attrName, HAPWrapperValue valueWrapper) {
		this();
		this.setName(attrName);
		this.setValueWrapper(valueWrapper);
	}

	public HAPWrapperValue getValueWrapper() {	return this.m_valueWrapper;	}
	public void setValueWrapper(HAPWrapperValue valueInfo) {	this.m_valueWrapper = valueInfo;	}

	public void addAdapter(HAPAdapter adapter) {    this.m_adapter.add(adapter);    }
	public List<HAPAdapter> getAdapters(){    return this.m_adapter;    }
	
	public void setValueOfValue(Object value) {		this.setValueWrapper(new HAPWrapperValueOfValue(value));	}
	public void setValueOfBrick(HAPBrick brick) {		this.setValueWrapper(new HAPWrapperValueOfBrick(brick));	}
	public void setValueOfResourceId(HAPResourceId resourceId) {		this.setValueWrapper(new HAPWrapperValueOfReferenceResource(resourceId));	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_valueWrapper!=null) {
			jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JSON));
		}
		
		List<String> adapterJsonList = new ArrayList<String>();
		for(HAPAdapter adapter : this.m_adapter) {
			adapterJsonList.add(adapter.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ADAPTER, HAPUtilityJson.buildArrayJson(adapterJsonList.toArray(new String[0])));
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.getValueWrapper().toStringValue(HAPSerializationFormat.JAVASCRIPT));

		List<String> adapterJsonList = new ArrayList<String>();
		for(HAPAdapter adapter : this.getAdapters()) {
			adapterJsonList.add(adapter.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ADAPTER, HAPUtilityJson.buildArrayJson(adapterJsonList.toArray(new String[0])));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_valueWrapper.buildResourceDependency(dependency, runtimeInfo);
		
		for(HAPAdapter adapter : this.getAdapters()) {
			adapter.buildResourceDependency(dependency, runtimeInfo);
		}
	}	
	
}
