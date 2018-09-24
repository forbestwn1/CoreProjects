package com.nosliw.uiresource.page.execute;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContextEntity;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;

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

	//context for tag
	private HAPContextGroupInUITag m_tagContext;
	private HAPContextFlat m_flatTagContext;

	private Map<String, HAPContextEntity> m_tagEvent;

	private Map<String, String> m_eventMapping;
	private Map<String, String> m_contextMapping;
	
	public HAPExecutableUIUnitTag(HAPDefinitionUIUnitTag uiTagDefinition) {
		super(uiTagDefinition);
		this.m_tagEvent = new LinkedHashMap<String, HAPContextEntity>();
		this.m_eventMapping = new LinkedHashMap<String, String>();
		this.m_contextMapping = new LinkedHashMap<String, String>();
	}

	public HAPContextGroup getTagContext(){  return this.m_tagContext;   }
	public void setTagContext(HAPContextGroup context) {
		if(this.m_tagContext!=null)   this.m_tagContext.clear();
		this.m_tagContext = new HAPContextGroupInUITag(this, context);
	}
	public HAPContextFlat getFlatTagContext() { return this.m_flatTagContext;  }
	public void setFlatTagContext(HAPContextFlat context) {  this.m_flatTagContext = context;   }
	public HAPContextFlat getTagVariableContext() {  return this.m_flatTagContext.getVariableContext();  }
	
	public void addTagEvent(String name, HAPContextEntity event) {  this.m_tagEvent.put(name, event);  }
	public Map<String, HAPContextEntity> getTagEvent(){  return this.m_tagEvent;  }
	
	public HAPDefinitionUIUnitTag getUIUnitTagDefinition() {   return (HAPDefinitionUIUnitTag)this.getUIUnitDefinition();  }
	
	public void setParent(HAPExecutableUIUnit parent) {		this.m_parent = parent;	}
	
	public void setEventMapping(Map<String, String> mapping) {  this.m_eventMapping.putAll(mapping);  }
	public void setContextMapping(Map<String, String> mapping) {  this.m_contextMapping.putAll(mapping);  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, this.getUIUnitTagDefinition().getTagName());
		jsonMap.put(TAGCONTEXT, this.getTagVariableContext().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EVENTMAPPING, HAPJsonUtility.buildMapJson(m_eventMapping));
		jsonMap.put(CONTEXTMAPPING, HAPJsonUtility.buildMapJson(m_contextMapping));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, this.getUIUnitTagDefinition().getTagName());
		jsonMap.put(TAGCONTEXT, this.getTagVariableContext().toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(EVENTMAPPING, HAPJsonUtility.buildMapJson(m_eventMapping));
		jsonMap.put(CONTEXTMAPPING, HAPJsonUtility.buildMapJson(m_contextMapping));
	}
}
