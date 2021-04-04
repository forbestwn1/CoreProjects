package com.nosliw.uiresource.page.definition;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.component.HAPContainerChildReferenceResource;
import com.nosliw.data.core.component.HAPNameMapping;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafValue;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;

/*
 * ui resource basic class for both ui resource and custom tag
 * it contains all the information after ui resource is compiled
 * it contains all the information within its domain
 * 		that means, for ui resource instance, it does not contains infor within customer tag
 */
public abstract class HAPDefinitionUIUnit extends HAPComponentImp{
 
	//parent ui unit
	private HAPDefinitionUIUnit m_parent;
	
	//all java script blocks within this unit
	private HAPJsonTypeScript m_script;

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
	
	private HAPDefinitionStyle m_style;
	
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

	//mapping from attachment name to internal name(service, resource, ...)
	private HAPNameMapping m_nameMapping;
	
	public HAPDefinitionUIUnit(String id){
		super(id);
		this.m_scriptExpressionsInAttribute = new HashSet<HAPDefinitionUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInTagAttribute = new HashSet<HAPDefinitionUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInContent = new HashSet<HAPDefinitionUIEmbededScriptExpressionInContent>();
		this.m_uiTags = new LinkedHashMap<String, HAPDefinitionUITag>();
		this.m_normalTagEvents = new HashSet<HAPElementEvent>();
		this.m_customTagEvents = new HashSet<HAPElementEvent>();
		this.m_attributes = new LinkedHashMap<String, String>();
//		this.m_expressionDefinitions = new LinkedHashMap<String, String>();
		this.m_eventsDefinition = new LinkedHashMap<String, HAPDefinitionUIEvent>();
		this.m_commandsDefinition = new LinkedHashMap<String, HAPDefinitionUICommand>();
		this.m_nameMapping = new HAPNameMapping();
		this.initContext();
	}
	
	private void initContext() {
		HAPContextGroup contextGroup = new HAPContextGroup();
		//ui error context element
		HAPContextDefinitionRoot uiValidationErrorRoot = new HAPContextDefinitionRoot(new HAPContextDefinitionLeafValue());
		uiValidationErrorRoot.setDefaultValue(new JSONObject());
//		contextGroup.addProtectedElement(HAPConstantShared.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR, uiValidationErrorRoot);
		this.setContextStructure(contextGroup);
	}
	
	abstract public String getType(); 

	public HAPDefinitionUIUnit getParent() {   return this.m_parent;  }
	public HAPJsonTypeScript getScriptBlock() {  return this.m_script;  }
	public Set<HAPElementEvent> getNormalTagEvents(){  return this.m_normalTagEvents;   }
	public Set<HAPElementEvent> getCustomTagEvents(){   return this.m_customTagEvents;   }
	public Map<String, String> getAttributes(){   return this.m_attributes;    }
	public String getContent() {  return this.m_content;  }
	public Map<String, HAPDefinitionUIEvent> getEventDefinitions(){  return this.m_eventsDefinition;    }
	public Map<String, HAPDefinitionUICommand> getCommandDefinition() {   return this.m_commandsDefinition;  }
//	public Map<String, String> getExpressionDefinitions(){  return this.m_expressionDefinitions;   }
	public HAPDefinitionStyle getStyle() {    return this.m_style;    }
	public void setStyle(HAPDefinitionStyle style) {   this.m_style = style;   }
	
	public void setParent(HAPDefinitionUIUnit parent) {	this.m_parent = parent;	}
	public void setJSBlock(HAPJsonTypeScript jsBlock){this.m_script = jsBlock;}
	public void setContent(String content){	this.m_content = content;	}
	public Collection<HAPDefinitionUITag> getUITags(){return this.m_uiTags.values();} 

	public Set<HAPDefinitionUIEmbededScriptExpressionInContent> getScriptExpressionsInContent(){   return this.m_scriptExpressionsInContent;   }
	public Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> getScriptExpressionsInAttribute(){   return this.m_scriptExpressionsInAttribute;    }
	public Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> getScriptExpressionsInTagAttribute(){   return this.m_scriptExpressionsInTagAttribute;   }
	
	public void addCommandDefinition(HAPDefinitionUICommand commandDef) {   this.m_commandsDefinition.put(commandDef.getName(), commandDef);   }
	public void addEventDefinition(HAPDefinitionUIEvent def) {  this.m_eventsDefinition.put(def.getName(), def);   }
//	public void addExpressionDefinition(String name, String expressionDef){		this.m_expressionDefinitions.put(name, expressionDef);	}
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
	
	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
//		HAPContainerChildResource out = new HAPContainerChildResource();
//		HAPUtilityComponent.buildServiceChildrenComponent(out, this, this.getAttachmentContainer());
//		return out;
		return null;
	}
}
