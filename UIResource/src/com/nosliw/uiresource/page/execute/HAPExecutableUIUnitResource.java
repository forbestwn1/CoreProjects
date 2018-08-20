package com.nosliw.uiresource.page.execute;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.script.context.HAPContextEntity;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;

public class HAPExecutableUIUnitResource extends HAPExecutableUIUnit{

	@HAPAttribute
	public static final String COMMANDS = "commands";

	private Map<String, HAPContextEntity> m_commandsDefinition;
	
	//all dependency resources
	private List<HAPResourceDependent> m_resourceDependency;

	
	public HAPExecutableUIUnitResource(HAPDefinitionUIUnit uiUnitDefinition) {
		super(uiUnitDefinition);
		this.m_resourceDependency = new ArrayList<HAPResourceDependent>();
		this.m_commandsDefinition = new LinkedHashMap<String, HAPContextEntity>();
	}

	public void addCommandDefinition(HAPContextEntity commandDef) {   this.m_commandsDefinition.put(commandDef.getName(), commandDef);   }
	public Map<String, HAPContextEntity> getCommandDefinition() {   return this.m_commandsDefinition;  }

	public List<HAPResourceDependent> getResourceDependency(){  return this.m_resourceDependency;  }

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(COMMANDS, HAPJsonUtility.buildJson(this.m_commandsDefinition, HAPSerializationFormat.JSON));
	}
}
