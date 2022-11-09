package com.nosliw.uiresource.page.execute;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPExecutableComponent;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPExecutableValueStructure;
import com.nosliw.data.core.valuestructure.HAPTreeNodeValueStructure;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUITagId;
import com.nosliw.uiresource.resource.HAPResourceIdUITag;

public class HAPExecutableUITag extends HAPExecutableComponent{
	
	@HAPAttribute
	public static final String TAGNAME = "tagName";

	@HAPAttribute
	public static final String TAGVALUESTRUCTURE = "tagValueStructure";

	@HAPAttribute
	public static final String EVENT = "event";

	@HAPAttribute
	public static final String EVENTMAPPING = "eventMapping";
	
	@HAPAttribute
	public static final String CONTEXTMAPPING = "contextMapping";

	@HAPAttribute
	public static final String COMMANDMAPPING = "commandMapping";

	@HAPAttribute
	public static final String SERVICEMAPPING = "serviceMapping";

	private String m_tagName;
	
	private HAPUITagDefinition m_uiTagDefinition;
	
	private HAPExecutableUIUnit m_uiUnit;
	

	//context for tag
	private HAPTreeNodeValueStructure m_tagValueStructureDefinitionNode;
	private HAPExecutableValueStructure m_tagValueStructureExe;

	private Map<String, HAPDefinitionUIEvent> m_tagEvent;
 
	private Map<String, String> m_eventMapping;
	private Map<String, String> m_contextMapping;
	private Map<String, String> m_commandMapping;
	private Map<String, String> m_serviceMapping;
	
	public HAPExecutableUITag(HAPDefinitionUITag uiTagDefinition, String id) {
		super(uiTagDefinition, id);
		this.m_tagValueStructureDefinitionNode = new HAPTreeNodeValueStructure();
		this.m_tagName = uiTagDefinition.getTagName();
		this.m_tagEvent = new LinkedHashMap<String, HAPDefinitionUIEvent>();
		this.m_eventMapping = new LinkedHashMap<String, String>();
		this.m_contextMapping = new LinkedHashMap<String, String>();
		this.m_commandMapping = new LinkedHashMap<String, String>();
		this.m_serviceMapping = new LinkedHashMap<String, String>();
	}

	public String getTagName() {    return this.m_tagName;   }
	
	public HAPTreeNodeValueStructure getTagValueStructureDefinitionNode(){  return this.m_tagValueStructureDefinitionNode;   }
	
	public HAPUITagDefinition getUITagDefinition() {   return this.m_uiTagDefinition;   }
	public void setUITagDefinition(HAPUITagDefinition uiTagDefinition) {    this.m_uiTagDefinition = uiTagDefinition;     }
	
	public void setTagValueStructureExe(HAPExecutableValueStructure context) {  this.m_tagValueStructureExe = context;   }
	public HAPExecutableValueStructure getTagValueStructureExe() {
		if(this.m_tagValueStructureExe!=null)		return this.m_tagValueStructureExe;
		else  return new HAPExecutableValueStructure();
	}
	
	public void addTagEvent(String name, HAPDefinitionUIEvent event) {  this.m_tagEvent.put(name, event);  }
	public Map<String, HAPDefinitionUIEvent> getTagEvent(){  return this.m_tagEvent;  }
	
	public HAPDefinitionUITag getUIUnitTagDefinition() {   return (HAPDefinitionUITag)this.getUIUnitDefinition();  }
	
	public void setEventMapping(Map<String, String> mapping) {  this.m_eventMapping.putAll(mapping);  }
	public void setContextMapping(Map<String, String> mapping) {  this.m_contextMapping.putAll(mapping);  }
	public void setCommandMapping(Map<String, String> mapping) {  this.m_commandMapping.putAll(mapping);  }
	public void setServiceMapping(Map<String, String> mapping) {  this.m_serviceMapping.putAll(mapping);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TAGNAME, this.getUIUnitTagDefinition().getTagName());
		jsonMap.put(CONTEXTMAPPING, HAPUtilityJson.buildMapJson(m_contextMapping));
		jsonMap.put(TAGVALUESTRUCTURE, this.getTagValueStructureExe().toStringValue(HAPSerializationFormat.JSON_FULL));
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENTMAPPING, HAPUtilityJson.buildMapJson(m_eventMapping));
		jsonMap.put(COMMANDMAPPING, HAPUtilityJson.buildMapJson(m_commandMapping));
		jsonMap.put(SERVICEMAPPING, HAPUtilityJson.buildMapJson(m_serviceMapping));
	}
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = super.getResourceDependency(runtimeInfo, resourceManager);
		//ui tag
		out.add(new HAPResourceDependency(new HAPResourceIdUITag(new HAPUITagId(this.m_tagName))));
		return out;
	}
}
