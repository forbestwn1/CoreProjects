package com.nosliw.uiresource.page.execute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.command.HAPExecutableCommand;
import com.nosliw.data.core.component.command.HAPReferenceCommand;
import com.nosliw.data.core.component.event.HAPExecutableEvent;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuiteImp;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.expression.HAPContextProcessExpressionScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.task.HAPExecutableTaskSuite;
import com.nosliw.data.core.valuestructure.HAPExecutableValueStructure;
import com.nosliw.data.core.valuestructure.HAPTreeNodeValueStructure;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPElementEvent;

@HAPEntityWithAttribute
public class HAPExecutableUIBody extends HAPExecutableImp{

	@HAPAttribute
	public static final String VALUESTRUCTURE = "valueStructure";
	@HAPAttribute
	public static final String SCRIPTEXPRESSIONSINCONTENT = "scriptExpressionsInContent";
	@HAPAttribute
	public static final String SCRIPTEXPRESSIONINATTRIBUTES = "scriptExpressionInAttributes";
	@HAPAttribute
	public static final String SCRIPTEXPRESSIONINTAGATTRIBUTES = "scriptExpressionTagAttributes";
	@HAPAttribute
	public static final String SCRIPTGROUP = "scriptGroup";
	@HAPAttribute
	public static final String SCRIPT = "script";
	@HAPAttribute
	public static final String HTML = "html";
	
	@HAPAttribute
	public static final String ELEMENTEVENTS = "elementEvents";
	@HAPAttribute
	public static final String TAGEVENTS = "tagEvents";
	@HAPAttribute
	public static final String UITAGS = "uiTags";
	@HAPAttribute
	public static final String UITAGLIBS = "uiTagLibs";
	@HAPAttribute
	public static final String CONSTANTS = "constants";
	@HAPAttribute
	public static final String EXPRESSIONS = "expressions";
	@HAPAttribute
	public static final String EVENTS = "events";
	@HAPAttribute
	public static final String HANDLERS = "handlers";
	@HAPAttribute
	public static final String SERVICES = "services";
	@HAPAttribute
	public static final String SERVICEPROVIDERS = "serviceProviders";
	@HAPAttribute
	public static final String COMMANDS = "commands";

	//context for content
	private HAPTreeNodeValueStructure m_valueStructureDefinitionNode;
//	private HAPExecutableValueStructure m_valueStructureExe;
	
//	private Map<String, Object> m_constants;
	
	//expression unit
	private HAPContextProcessExpressionScript m_processScriptContext;
	
	//all the customer tag within the domain
	private Map<String, HAPExecutableUIUnitTag> m_uiTags; 
	
	//all the expressions within content under this domain
	private Set<HAPUIEmbededScriptExpressionInContent> m_scriptExpressionsInContent;
	//all the attribute expressions in regular tag under this domain 
	private Set<HAPUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInAttribute;
	//all the attribute expressions in customer tag under this domain
	private Set<HAPUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInTagAttribute;

	//executable script group
	private HAPExecutableScriptGroup m_scriptGroupExe;
	
	//event definition 
	private List<HAPExecutableEvent> m_events;
	private List<HAPExecutableCommand> m_commands;

	private List<HAPReferenceCommand> m_commandReferences;

	private HAPExecutableTaskSuite m_handlers;
	
	//service requirement definition
	private Map<String, HAPExecutableServiceUse> m_services;

	private Set<HAPElementEvent> m_elementEvents;
	private Set<HAPElementEvent> m_tagEvents;

	//all java script blocks within this unit
	private HAPJsonTypeScript m_script;
	
	private String m_html;
	
	public HAPExecutableUIBody(HAPDefinitionUIUnit uiUnitDefinition, HAPExecutableUIUnit uiUnit) {
		this.m_elementEvents = uiUnitDefinition.getNormalTagEvents();
		this.m_tagEvents = uiUnitDefinition.getCustomTagEvents();
		this.m_script = uiUnitDefinition.getScriptBlock();
		
		this.m_html = StringEscapeUtils.escapeHtml(uiUnitDefinition.getContent()).replaceAll("(\\r|\\n)", "");
		
		this.m_scriptExpressionsInContent = new HashSet<HAPUIEmbededScriptExpressionInContent>();
		this.m_scriptExpressionsInAttribute = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInTagAttribute = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		this.m_processScriptContext = new HAPContextProcessExpressionScript();
		this.m_uiTags = new LinkedHashMap<String, HAPExecutableUIUnitTag>();
		this.m_valueStructureDefinitionNode = new HAPTreeNodeValueStructure();
//		this.m_constants = new LinkedHashMap<String, Object>();
		
		this.m_events = new ArrayList<HAPExecutableEvent>();
		this.m_commands = new ArrayList<HAPExecutableCommand>();

		this.m_services = new LinkedHashMap<String, HAPExecutableServiceUse>();
		
		//build tag trees according to definition
		for(HAPDefinitionUITag tag : uiUnitDefinition.getUITags()) {
			HAPExecutableUIUnitTag exeTag = new HAPExecutableUIUnitTag(tag, tag.getId());
			this.m_uiTags.put(tag.getId(), exeTag);
			exeTag.setParent(uiUnit);
		}
	}
	
	public HAPTreeNodeValueStructure getValueStructureDefinitionNode(){  return this.m_valueStructureDefinitionNode;   }
	
	
//	public void setValueStructureExe(HAPExecutableValueStructure context) {  this.m_valueStructureExe = context;   }
	public HAPExecutableValueStructure getValueStructureExe() {
		return HAPUtilityValueStructure.buildExecuatableValueStructure(this.getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure());
	}
	
//	public Map<String, Object> getConstantsValue(){   return this.m_processScriptContext.getConstantsValue();    }
//	public void addConstantValue(String name, Object value) {		this.m_processScriptContext.addConstant(name, value);	}

	public HAPContextProcessExpressionScript getProcessExpressionScriptContext(){   return this.m_processScriptContext;   }
	public void setProcessExpressionScriptContext(HAPContextProcessExpressionScript context){  this.m_processScriptContext = context;   }

	public HAPDefinitionExpressionSuiteImp getExpressionSuiteInContext() {    return (HAPDefinitionExpressionSuiteImp)this.m_processScriptContext.getExpressionDefinitionSuite();   }
	
	public Collection<HAPExecutableUIUnitTag> getUITags(){return this.m_uiTags.values();} 
	public HAPExecutableUIUnitTag getUITag(String id){return this.m_uiTags.get(id);} 
	public Map<String, HAPExecutableUIUnitTag> getUITagesByName(){   return this.m_uiTags;   }
	
	public void addScriptExpressionsInContent(HAPUIEmbededScriptExpressionInContent se) {this.m_scriptExpressionsInContent.add(se);}
	public void addScriptExpressionsInAttribute(HAPUIEmbededScriptExpressionInAttribute se) { this.m_scriptExpressionsInAttribute.add(se);   }
	public void addScriptExpressionsInTagAttribute(HAPUIEmbededScriptExpressionInAttribute se) { this.m_scriptExpressionsInTagAttribute.add(se);   }

	public Set<HAPUIEmbededScriptExpressionInContent> getScriptExpressionsInContent() {  return this.m_scriptExpressionsInContent;  }
	public Set<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionsInAttribute() {  return this.m_scriptExpressionsInAttribute;   }
	public Set<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionsInTagAttribute() {  return this.m_scriptExpressionsInTagAttribute;   }

	public HAPExecutableScriptGroup getScriptGroupExecutable() {   return this.m_scriptGroupExe;    }
	public void setScriptGroupExecutable(HAPExecutableScriptGroup scriptGroupExe) {   this.m_scriptGroupExe = scriptGroupExe;     }
	
	public List<HAPExecutableEvent> getEvents(){  return this.m_events;    }
	public HAPExecutableEvent getEventDefinition(String name) {   
		for(HAPExecutableEvent event : this.m_events) {
			if(name.equals(event.getName()))   return event;
		}
		return null;  
	}
	public void addEventDefinition(HAPExecutableEvent event) {  this.m_events.add(event);    }
	
	public void addServiceUse(String name, HAPExecutableServiceUse serviceDef) {   this.m_services.put(name, serviceDef);   }
	public Map<String, HAPExecutableServiceUse> getServiceUses(){  return this.m_services;   }
	public HAPExecutableServiceUse getServiceUse(String name) {   return this.m_services.get(name);  }

	public void addCommand(HAPExecutableCommand commandDef) {   this.m_commands.add(commandDef);   }
	public List<HAPExecutableCommand> getCommands() {   return this.m_commands;  }
//	public HAPDefinitionUICommand getCommandDefinition(String name) {   return this.m_commands.get(name);  }

	public HAPExecutableTaskSuite getHandlers() {    return this.m_handlers;     }
	public void setHandlers(HAPExecutableTaskSuite handlers) {     this.m_handlers = handlers;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VALUESTRUCTURE, this.getValueStructureExe().toStringValue(HAPSerializationFormat.JSON));
		
		List<String> eleEventsJsons = new ArrayList<String>();
		for(HAPElementEvent elementEvent : this.m_elementEvents)  eleEventsJsons.add(elementEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTEVENTS, HAPJsonUtility.buildArrayJson(eleEventsJsons.toArray(new String[0])));
		
		List<String> tagEvents = new ArrayList<String>();
		for(HAPElementEvent tagEvent : this.m_tagEvents)		tagEvents.add(tagEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TAGEVENTS, HAPJsonUtility.buildArrayJson(tagEvents.toArray(new String[0])));
		
		jsonMap.put(HTML, this.m_html);
		
//		jsonMap.put(CONSTANTS, HAPJsonUtility.buildJson(this.m_processScriptContext.getConstantsValue(), HAPSerializationFormat.JSON));
	
		jsonMap.put(EVENTS, HAPJsonUtility.buildJson(this.m_events, HAPSerializationFormat.JSON));

		Map<String, String> commandJsonMap = new LinkedHashMap<String, String>();
		for(HAPExecutableCommand command : this.m_commands) commandJsonMap.put(command.getName(), command.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(COMMANDS, HAPJsonUtility.buildMapJson(commandJsonMap));

		jsonMap.put(HANDLERS, this.m_handlers.toStringValue(HAPSerializationFormat.JSON));
		
		jsonMap.put(SERVICES, HAPJsonUtility.buildJson(this.m_services, HAPSerializationFormat.JSON));

		List<String> expressionContentJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInContent expressionContent : this.m_scriptExpressionsInContent)  expressionContentJsons.add(expressionContent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(SCRIPTEXPRESSIONSINCONTENT, HAPJsonUtility.buildArrayJson(expressionContentJsons.toArray(new String[0])));
		
		List<String> expressionAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionAttr : this.m_scriptExpressionsInAttribute)  expressionAttributeJsons.add(expressionAttr.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(SCRIPTEXPRESSIONINATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionAttributeJsons.toArray(new String[0])));

		List<String> expressionTagAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionTagAttr : this.m_scriptExpressionsInTagAttribute)  expressionTagAttributeJsons.add(expressionTagAttr.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(SCRIPTEXPRESSIONINTAGATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionTagAttributeJsons.toArray(new String[0])));

		if(m_scriptGroupExe!=null)  jsonMap.put(SCRIPTGROUP, this.m_scriptGroupExe.toStringValue(HAPSerializationFormat.JSON));
		
		Map<String, String> uiTagJsons = new LinkedHashMap<String, String>();
		for(String uiId : this.m_uiTags.keySet())	uiTagJsons.put(uiId, this.m_uiTags.get(uiId).toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(UITAGS, HAPJsonUtility.buildMapJson(uiTagJsons));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		HAPJsonTypeScript script = this.m_script;
		if(script!=null){
			jsonMap.put(SCRIPT, script.toStringValue(HAPSerializationFormat.JSON_FULL));
			typeJsonMap.put(SCRIPT, script.getClass());
		}

//		List<String> expressionContentJsons = new ArrayList<String>();
//		for(HAPUIEmbededScriptExpressionInContent expressionContent : this.m_scriptExpressionsInContent)  expressionContentJsons.add(expressionContent.toResourceData(runtimeInfo).toString());
//		jsonMap.put(SCRIPTEXPRESSIONSINCONTENT, HAPJsonUtility.buildArrayJson(expressionContentJsons.toArray(new String[0])));
//		
//		List<String> expressionAttributeJsons = new ArrayList<String>();
//		for(HAPUIEmbededScriptExpressionInAttribute expressionAttr : this.m_scriptExpressionsInAttribute)  expressionAttributeJsons.add(expressionAttr.toResourceData(runtimeInfo).toString());
//		jsonMap.put(SCRIPTEXPRESSIONINATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionAttributeJsons.toArray(new String[0])));
//
//		List<String> expressionTagAttributeJsons = new ArrayList<String>();
//		for(HAPUIEmbededScriptExpressionInAttribute expressionTagAttr : this.m_scriptExpressionsInTagAttribute)  expressionTagAttributeJsons.add(expressionTagAttr.toResourceData(runtimeInfo).toString());
//		jsonMap.put(SCRIPTEXPRESSIONINTAGATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionTagAttributeJsons.toArray(new String[0])));

		Map<String, String> commandJsonMap = new LinkedHashMap<String, String>();
		for(HAPExecutableCommand command : this.m_commands) commandJsonMap.put(command.getName(), command.toResourceData(runtimeInfo).toString());
		jsonMap.put(COMMANDS, HAPJsonUtility.buildMapJson(commandJsonMap));

		jsonMap.put(SCRIPTGROUP, this.m_scriptGroupExe.toResourceData(runtimeInfo).toString());
		
		jsonMap.put(HANDLERS, this.m_handlers.toResourceData(runtimeInfo).toString());

		Map<String, String> serviceResourceMap = new LinkedHashMap<String, String>();
		for(String serviceName : this.m_services.keySet()) 	serviceResourceMap.put(serviceName, this.m_services.get(serviceName).toResourceData(runtimeInfo).toString());
		jsonMap.put(SERVICES, HAPJsonUtility.buildMapJson(serviceResourceMap));
		
		Map<String, String> uiTagJsons = new LinkedHashMap<String, String>();
		for(String uiId : this.m_uiTags.keySet())	uiTagJsons.put(uiId, this.m_uiTags.get(uiId).toResourceData(runtimeInfo).toString());
		jsonMap.put(UITAGS, HAPJsonUtility.buildMapJson(uiTagJsons));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		//resource from expression
		dependency.addAll(this.m_scriptGroupExe.getResourceDependency(runtimeInfo, resourceManager));
		
		//resource from child uiTag
		for(String uiId : this.m_uiTags.keySet()) dependency.addAll(this.m_uiTags.get(uiId).getResourceDependency(runtimeInfo, resourceManager));	
	}
}
