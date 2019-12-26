package com.nosliw.uiresource.page.definition;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPScript;
import com.nosliw.data.core.resource.external.HAPNameMapping;
import com.nosliw.data.core.resource.external.HAPWithExternalMappingImp;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.service.use.HAPDefinitionServiceInEntity;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceProvider;

/*
 * ui resource basic class for both ui resource and custom tag
 * it contains all the information after ui resource is compiled
 * it contains all the information within its domain
 * 		that means, for ui resource instance, it does not contains infor within customer tag
 */
public abstract class HAPDefinitionUIUnit extends HAPWithExternalMappingImp implements HAPWithServiceProvider{
 
	//for tag, it is tag id within resource
	//for resource, it is resource name
	private String m_id;

	//parent ui unit
	private HAPDefinitionUIUnit m_parent;
	
	//context definition within this unit
	private HAPContextGroup m_contextDefinition;
	
	//all java script blocks within this unit
	private HAPScript m_script;

	//expression definition within this unit
	private Map<String, String> m_expressionDefinitions;

	//html content after compilation
	private String m_content; 
	
	//all the events related with regular tag
	private Set<HAPElementEvent> m_normalTagEvents;

	//all the events related with customer tag
	private Set<HAPElementEvent> m_customTagEvents;
	
	//store all the constant attribute for this domain
	//for customer tag, they are the tag's attribute
	//for resource, they are the attribute of body
	private Map<String, String> m_attributes;
	
	//all the expressions within content under this domain
	private Set<HAPDefinitionUIEmbededScriptExpressionInContent> m_scriptExpressionsInContent;
	//all the attribute expressions in regular tag under this domain 
	private Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInAttribute;
	//all the attribute expressions in customer tag under this domain
	private Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInTagAttribute;
	
	//all the customer tag within the domain
	private Map<String, HAPDefinitionUITag> m_uiTags; 

	//event definition 
	private Map<String, HAPDefinitionUIEvent> m_eventsDefinition;
	//command definition
	private Map<String, HAPDefinitionUICommand> m_commandsDefinition;
	//service definition
	private HAPDefinitionServiceInEntity m_serviceDefinition;

	//mapping from external name to internal name(service, resource, ...)
	private HAPNameMapping m_nameMapping;
	
	public HAPDefinitionUIUnit(String id){
		this.m_id = id;
		this.m_contextDefinition = new HAPContextGroup();
		this.m_scriptExpressionsInAttribute = new HashSet<HAPDefinitionUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInTagAttribute = new HashSet<HAPDefinitionUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInContent = new HashSet<HAPDefinitionUIEmbededScriptExpressionInContent>();
		this.m_uiTags = new LinkedHashMap<String, HAPDefinitionUITag>();
		this.m_normalTagEvents = new HashSet<HAPElementEvent>();
		this.m_customTagEvents = new HashSet<HAPElementEvent>();
		this.m_attributes = new LinkedHashMap<String, String>();
		this.m_expressionDefinitions = new LinkedHashMap<String, String>();
		this.m_eventsDefinition = new LinkedHashMap<String, HAPDefinitionUIEvent>();
		this.m_commandsDefinition = new LinkedHashMap<String, HAPDefinitionUICommand>();
		this.m_serviceDefinition = new HAPDefinitionServiceInEntity();
		this.m_nameMapping = new HAPNameMapping();
	}
	
	abstract public String getType(); 

	public HAPDefinitionUIUnit getParent() {   return this.m_parent;  }
	public String getId(){return this.m_id;}
	public HAPScript getScriptBlock() {  return this.m_script;  }
	public Set<HAPElementEvent> getNormalTagEvents(){  return this.m_normalTagEvents;   }
	public Set<HAPElementEvent> getCustomTagEvents(){   return this.m_customTagEvents;   }
	public Map<String, String> getAttributes(){   return this.m_attributes;    }
	public String getContent() {  return this.m_content;  }
	public Map<String, HAPDefinitionUIEvent> getEventDefinitions(){  return this.m_eventsDefinition;    }
	public HAPDefinitionServiceInEntity getServiceDefinition() {    return this.m_serviceDefinition;    }
	public Map<String, HAPDefinitionServiceUse> getServiceUseDefinitions(){  return this.m_serviceDefinition.getServiceUseDefinitions();   }
	@Override
	public Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions(){  return this.m_serviceDefinition.getServiceProviderDefinitions();   }
	public Map<String, HAPDefinitionUICommand> getCommandDefinition() {   return this.m_commandsDefinition;  }
	public Map<String, String> getExpressionDefinitions(){  return this.m_expressionDefinitions;   }
	
	public void setParent(HAPDefinitionUIUnit parent) {	this.m_parent = parent;	}
	public void setJSBlock(HAPScript jsBlock){this.m_script = jsBlock;}
	public void setContent(String content){	this.m_content = content;	}
	public HAPContextGroup getContextDefinition(){  return this.m_contextDefinition;   }
	public Collection<HAPDefinitionUITag> getUITags(){return this.m_uiTags.values();} 

	public Set<HAPDefinitionUIEmbededScriptExpressionInContent> getScriptExpressionsInContent(){   return this.m_scriptExpressionsInContent;   }
	public Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> getScriptExpressionsInAttribute(){   return this.m_scriptExpressionsInAttribute;    }
	public Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> getScriptExpressionsInTagAttribute(){   return this.m_scriptExpressionsInTagAttribute;   }
	
	public void addCommandDefinition(HAPDefinitionUICommand commandDef) {   this.m_commandsDefinition.put(commandDef.getName(), commandDef);   }
	public void addEventDefinition(HAPDefinitionUIEvent def) {  this.m_eventsDefinition.put(def.getName(), def);   }
	public void addServiceUseDefinition(HAPDefinitionServiceUse def) {  this.m_serviceDefinition.addServiceUseDefinition(def);   }
	@Override
	public void addServiceProviderDefinition(HAPDefinitionServiceProvider def) {  this.m_serviceDefinition.addServiceProviderDefinition(def);   }
	public void addExpressionDefinition(String name, String expressionDef){		this.m_expressionDefinitions.put(name, expressionDef);	}
	public void addScriptExpressionInAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute eAttr){	this.m_scriptExpressionsInAttribute.add(eAttr);	}
	public void addScriptExpressionInTagAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute eAttr){	this.m_scriptExpressionsInTagAttribute.add(eAttr);	}
	public void addScriptExpressionInContent(HAPDefinitionUIEmbededScriptExpressionInContent scriptExpressionInContent){	this.m_scriptExpressionsInContent.add(scriptExpressionInContent);	}
	public void addUITag(HAPDefinitionUITag uiTag){	
		this.m_uiTags.put(uiTag.getId(), uiTag);	
		uiTag.setParent(uiTag);
	}
	public void addTagEvent(HAPElementEvent event){this.m_customTagEvents.add(event);}
	public void addElementEvent(HAPElementEvent event){this.m_normalTagEvents.add(event);}
	public void addAttribute(String name, String value){		this.m_attributes.put(name, value);	}
	
	public void setNameMapping(HAPNameMapping nameMapping) {   this.m_nameMapping = nameMapping;   }
	public HAPNameMapping getNameMapping(){   return this.m_nameMapping;    }
	
	public void postRead(){}
}
