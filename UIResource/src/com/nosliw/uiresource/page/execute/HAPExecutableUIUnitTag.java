package com.nosliw.uiresource.page.execute;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.resource.HAPResourceIdUITag;
import com.nosliw.uiresource.tag.HAPUITagId;

public class HAPExecutableUIUnitTag extends HAPExecutableUIUnit{

	@HAPAttribute
	public static final String TAGNAME = "tagName";

	@HAPAttribute
	public static final String TAGCONTEXT = "tagContext";

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
	
	//context for tag
	private HAPContextGroupInUITag m_tagContext;
	private HAPContextFlat m_flatTagContext;

	private Map<String, HAPDefinitionUIEvent> m_tagEvent;

	private Map<String, String> m_eventMapping;
	private Map<String, String> m_contextMapping;
	private Map<String, String> m_commandMapping;
	private Map<String, String> m_serviceMapping;
	
	public HAPExecutableUIUnitTag(HAPDefinitionUIUnitTag uiTagDefinition, String id) {
		super(uiTagDefinition, id);
		this.m_tagEvent = new LinkedHashMap<String, HAPDefinitionUIEvent>();
		this.m_eventMapping = new LinkedHashMap<String, String>();
		this.m_contextMapping = new LinkedHashMap<String, String>();
		this.m_commandMapping = new LinkedHashMap<String, String>();
		this.m_serviceMapping = new LinkedHashMap<String, String>();
	}

	public HAPContextGroup getTagContext(){  return this.m_tagContext;   }
	public void setTagContext(HAPContextGroup context) {
		if(this.m_tagContext!=null)   this.m_tagContext.clear();
		this.m_tagContext = new HAPContextGroupInUITag(this, context);
	}
	public HAPContextFlat getFlatTagContext() { return this.m_flatTagContext;  }
	public void setFlatTagContext(HAPContextFlat context) {  this.m_flatTagContext = context;   }
	public HAPContextFlat getTagVariableContext() {
		if(this.m_flatTagContext!=null)		return this.m_flatTagContext.getVariableContext();
		else  return new HAPContextFlat();
	}
	
	public void addTagEvent(String name, HAPDefinitionUIEvent event) {  this.m_tagEvent.put(name, event);  }
	public Map<String, HAPDefinitionUIEvent> getTagEvent(){  return this.m_tagEvent;  }
	
	public HAPDefinitionUIUnitTag getUIUnitTagDefinition() {   return (HAPDefinitionUIUnitTag)this.getUIUnitDefinition();  }
	
	public void setParent(HAPExecutableUIUnit parent) {		this.m_parent = parent;	}
	
	public void setEventMapping(Map<String, String> mapping) {  this.m_eventMapping.putAll(mapping);  }
	public void setContextMapping(Map<String, String> mapping) {  this.m_contextMapping.putAll(mapping);  }
	public void setCommandMapping(Map<String, String> mapping) {  this.m_commandMapping.putAll(mapping);  }
	public void setServiceMapping(Map<String, String> mapping) {  this.m_serviceMapping.putAll(mapping);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TAGNAME, this.getUIUnitTagDefinition().getTagName());
		jsonMap.put(CONTEXTMAPPING, HAPJsonUtility.buildMapJson(m_contextMapping));
		jsonMap.put(TAGCONTEXT, this.getTagVariableContext().toStringValue(HAPSerializationFormat.JSON_FULL));
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENTMAPPING, HAPJsonUtility.buildMapJson(m_eventMapping));
		jsonMap.put(COMMANDMAPPING, HAPJsonUtility.buildMapJson(m_commandMapping));
		jsonMap.put(SERVICEMAPPING, HAPJsonUtility.buildMapJson(m_serviceMapping));
	}
	
	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = super.getResourceDependency(runtimeInfo);
		//ui tag
		out.add(new HAPResourceDependent(new HAPResourceIdUITag(new HAPUITagId(this.getUIUnitTagDefinition().getTagName()))));
		return out;
	}

}
