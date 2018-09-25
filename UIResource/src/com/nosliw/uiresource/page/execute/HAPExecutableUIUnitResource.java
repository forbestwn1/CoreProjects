package com.nosliw.uiresource.page.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;

public class HAPExecutableUIUnitResource extends HAPExecutableUIUnit{

	//all dependency resources
	private List<HAPResourceDependent> m_resourceDependency;

	
	public HAPExecutableUIUnitResource(HAPDefinitionUIUnit uiUnitDefinition) {
		super(uiUnitDefinition);
		this.m_resourceDependency = new ArrayList<HAPResourceDependent>();
	}

	public List<HAPResourceDependent> getResourceDependency(){  return this.m_resourceDependency;  }

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
