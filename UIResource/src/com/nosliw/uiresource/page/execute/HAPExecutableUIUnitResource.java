package com.nosliw.uiresource.page.execute;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.resource.HAPResourceIdUITag;
import com.nosliw.uiresource.tag.HAPUITagId;

public class HAPExecutableUIUnitResource extends HAPExecutableUIUnit{

	//all dependency resources
	private List<HAPResourceDependent> m_resourceDependency;

	
	public HAPExecutableUIUnitResource(HAPDefinitionUIUnit uiUnitDefinition, String id) {
		super(uiUnitDefinition, id);
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
