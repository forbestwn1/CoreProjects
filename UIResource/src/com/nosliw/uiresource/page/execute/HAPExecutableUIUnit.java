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
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.expression.HAPProcessContextScriptExpression;
import com.nosliw.data.core.service.HAPDefinitionServiceInfo;
import com.nosliw.uiresource.page.definition.HAPDefinitionUICommand;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.definition.HAPElementEvent;

@HAPEntityWithAttribute(baseName="UIRESOURCEDEFINITION")
public class HAPExecutableUIUnit extends HAPSerializableImp implements HAPExecutable{

	@HAPAttribute
	public static final String ID = "id";
	@HAPAttribute
	public static final String CONTEXT = "context";
	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String SCRIPTEXPRESSIONSINCONTENT = "scriptExpressionsInContent";
	@HAPAttribute
	public static final String SCRIPTEXPRESSIONINATTRIBUTES = "scriptExpressionInAttributes";
	@HAPAttribute
	public static final String SCRIPTEXPRESSIONINTAGATTRIBUTES = "scriptExpressionTagAttributes";
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
	public static final String ATTRIBUTES = "attributes";
	@HAPAttribute
	public static final String UITAGLIBS = "uiTagLibs";
	@HAPAttribute
	public static final String CONSTANTS = "constants";
	@HAPAttribute
	public static final String EXPRESSIONS = "expressions";
	@HAPAttribute
	public static final String EVENTS = "events";
	@HAPAttribute
	public static final String SERVICES = "services";
	@HAPAttribute
	public static final String COMMANDS = "commands";

	private String m_id;
	
	private HAPDefinitionUIUnit m_uiUnitDefinition;

	protected HAPExecutableUIUnit m_parent;
	
	//context for content
	private HAPContextGroupInUIResource m_context;
	private HAPContextFlat m_flatContext;
	
//	private Map<String, Object> m_constants;
	
	//store all the constant attribute for this domain
	//for customer tag, they are the tag's attribute
	//for resource, they are the attribute of body
	private Map<String, String> m_attributes;
	
	//expression unit
	private HAPProcessContextScriptExpression m_scriptExpressionContext;
	
	//all the customer tag within the domain
	private Map<String, HAPExecutableUIUnitTag> m_uiTags; 
	
	//all the expressions within content under this domain
	private Set<HAPUIEmbededScriptExpressionInContent> m_scriptExpressionsInContent;
	//all the attribute expressions in regular tag under this domain 
	private Set<HAPUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInAttribute;
	//all the attribute expressions in customer tag under this domain
	private Set<HAPUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInTagAttribute;

	//event definition 
	private Map<String, HAPDefinitionUIEvent> m_eventsDefinition;
	private Map<String, HAPDefinitionUICommand> m_commandsDefinition;
	
	//service requirement definition
	private Map<String, HAPDefinitionServiceInfo> m_servicesDefinition;
	
	public HAPExecutableUIUnit(HAPDefinitionUIUnit uiUnitDefinition, String id) {
		this.m_id = id;
		this.m_uiUnitDefinition = uiUnitDefinition;
		this.m_scriptExpressionsInContent = new HashSet<HAPUIEmbededScriptExpressionInContent>();
		this.m_scriptExpressionsInAttribute = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInTagAttribute = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionContext = new HAPProcessContextScriptExpression();
		this.m_uiTags = new LinkedHashMap<String, HAPExecutableUIUnitTag>();
		this.m_context = new HAPContextGroupInUIResource(this);
		this.m_attributes = new LinkedHashMap<String, String>();
//		this.m_constants = new LinkedHashMap<String, Object>();
		
		this.m_eventsDefinition = new LinkedHashMap<String, HAPDefinitionUIEvent>();
		this.m_commandsDefinition = new LinkedHashMap<String, HAPDefinitionUICommand>();

		this.m_servicesDefinition = new LinkedHashMap<String, HAPDefinitionServiceInfo>();
		
		//build tag trees according to definition
		for(HAPDefinitionUIUnitTag tag : uiUnitDefinition.getUITags()) {
			HAPExecutableUIUnitTag exeTag = new HAPExecutableUIUnitTag(tag, uiUnitDefinition.getId());
			this.m_uiTags.put(tag.getId(), exeTag);
			exeTag.setParent(this);
		}
	}
	
	public String getType() {  return this.m_uiUnitDefinition.getType();  }

	public String getId() {   return this.m_id;    }
	
	public HAPExecutableUIUnit getParent() {  return this.m_parent;   }
	public void setParent(HAPExecutableUIUnit parent) {		this.m_parent = parent;	}
	public HAPContextGroup getContext(){  return this.m_context;   }
	public void setContext(HAPContextGroup context) {
		this.m_context.clear();
		this.m_context = new HAPContextGroupInUIResource(this, context);
	}
	public HAPContextFlat getFlatContext() { return this.m_flatContext;  }
	public void setFlatContext(HAPContextFlat context) {  this.m_flatContext = context;   }
	public HAPContextFlat getVariableContext() {
		if(this.m_flatContext!=null)		return this.m_flatContext.getVariableContext();
		else return new HAPContextFlat();
	}
	
	public Map<String, Object> getConstantsValue(){   return this.m_scriptExpressionContext.getConstants();    }
	public void addConstantValue(String name, Object value) {		this.m_scriptExpressionContext.addConstant(name, value);	}

	public Map<String, String> getAttributes(){   return this.m_attributes;    }
	public void addAttribute(String name, String value) {   this.m_attributes.put(name, value);   }

	public HAPDefinitionUIUnit getUIUnitDefinition() {	return this.m_uiUnitDefinition;	}
	
	public HAPProcessContextScriptExpression getExpressionContext(){   return this.m_scriptExpressionContext;   }
	public void setExpressionContext(HAPProcessContextScriptExpression context){  this.m_scriptExpressionContext = context;   }

	public Collection<HAPExecutableUIUnitTag> getUITags(){return this.m_uiTags.values();} 
	public HAPExecutableUIUnitTag getUITag(String id){return this.m_uiTags.get(id);} 
	public Map<String, HAPExecutableUIUnitTag> getUITagesByName(){   return this.m_uiTags;   }
	
	public void addScriptExpressionsInContent(HAPUIEmbededScriptExpressionInContent se) {this.m_scriptExpressionsInContent.add(se);}
	public void addScriptExpressionsInAttribute(HAPUIEmbededScriptExpressionInAttribute se) { this.m_scriptExpressionsInAttribute.add(se);   }
	public void addScriptExpressionsInTagAttribute(HAPUIEmbededScriptExpressionInAttribute se) { this.m_scriptExpressionsInTagAttribute.add(se);   }

	public Set<HAPUIEmbededScriptExpressionInContent> getScriptExpressionsInContent() {  return this.m_scriptExpressionsInContent;  }
	public Set<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionsInAttribute() {  return this.m_scriptExpressionsInAttribute;   }
	public Set<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionsInTagAttribute() {  return this.m_scriptExpressionsInTagAttribute;   }

	public Map<String, HAPDefinitionUIEvent> getEventDefinitions(){  return this.m_eventsDefinition;    }
	public HAPDefinitionUIEvent getEventDefinition(String name) {   return this.m_eventsDefinition.get(name);  }
	public void addEventDefinition(HAPDefinitionUIEvent eventDef) {  this.m_eventsDefinition.put(eventDef.getName(), eventDef);    }
	
	public void addServiceDefinition(HAPDefinitionServiceInfo serviceDef) {   this.m_servicesDefinition.put(serviceDef.getName(), serviceDef);   }
	public Map<String, HAPDefinitionServiceInfo> getServiceDefinitions(){  return this.m_servicesDefinition;   }
	public HAPDefinitionServiceInfo getServiceDefinition(String name) {   return this.m_servicesDefinition.get(name);  }

	public void addCommandDefinition(HAPDefinitionUICommand commandDef) {   this.m_commandsDefinition.put(commandDef.getName(), commandDef);   }
	public Map<String, HAPDefinitionUICommand> getCommandDefinitions() {   return this.m_commandsDefinition;  }
	public HAPDefinitionUICommand getCommandDefinition(String name) {   return this.m_commandsDefinition.get(name);  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.getId());
		jsonMap.put(TYPE, String.valueOf(this.getType()));

		jsonMap.put(CONTEXT, this.getVariableContext().toStringValue(HAPSerializationFormat.JSON));
		
		List<String> eleEventsJsons = new ArrayList<String>();
		for(HAPElementEvent elementEvent : this.m_uiUnitDefinition.getNormalTagEvents())  eleEventsJsons.add(elementEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTEVENTS, HAPJsonUtility.buildArrayJson(eleEventsJsons.toArray(new String[0])));
		
		List<String> tagEvents = new ArrayList<String>();
		for(HAPElementEvent tagEvent : this.m_uiUnitDefinition.getCustomTagEvents())		tagEvents.add(tagEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TAGEVENTS, HAPJsonUtility.buildArrayJson(tagEvents.toArray(new String[0])));
		
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildMapJson(this.m_uiUnitDefinition.getAttributes()));
		
		String htmlContent = StringEscapeUtils.escapeHtml(this.m_uiUnitDefinition.getContent());
		htmlContent = htmlContent.replaceAll("(\\r|\\n)", "");
		jsonMap.put(HTML, htmlContent);
		
		jsonMap.put(CONSTANTS, HAPJsonUtility.buildJson(this.m_scriptExpressionContext.getConstants(), HAPSerializationFormat.JSON));
	
		jsonMap.put(EVENTS, HAPJsonUtility.buildJson(this.m_eventsDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(COMMANDS, HAPJsonUtility.buildJson(this.m_commandsDefinition, HAPSerializationFormat.JSON));

		jsonMap.put(SERVICES, HAPJsonUtility.buildJson(this.m_servicesDefinition, HAPSerializationFormat.JSON));

		
		
		List<String> expressionContentJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInContent expressionContent : this.m_scriptExpressionsInContent)  expressionContentJsons.add(expressionContent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(SCRIPTEXPRESSIONSINCONTENT, HAPJsonUtility.buildArrayJson(expressionContentJsons.toArray(new String[0])));
		
		List<String> expressionAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionAttr : this.m_scriptExpressionsInAttribute)  expressionAttributeJsons.add(expressionAttr.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(SCRIPTEXPRESSIONINATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionAttributeJsons.toArray(new String[0])));

		List<String> expressionTagAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionTagAttr : this.m_scriptExpressionsInTagAttribute)  expressionTagAttributeJsons.add(expressionTagAttr.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(SCRIPTEXPRESSIONINTAGATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionTagAttributeJsons.toArray(new String[0])));

	
		Map<String, String> uiTagJsons = new LinkedHashMap<String, String>();
		for(String uiId : this.m_uiTags.keySet())	uiTagJsons.put(uiId, this.m_uiTags.get(uiId).toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(UITAGS, HAPJsonUtility.buildMapJson(uiTagJsons));
	}
	
	protected void buildMyJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPSerializationFormat format){
		jsonMap.put(ID, this.getId());
		jsonMap.put(TYPE, String.valueOf(this.getType()));

		jsonMap.put(CONTEXT, this.getVariableContext().toStringValue(format));
		
		List<String> eleEventsJsons = new ArrayList<String>();
		for(HAPElementEvent elementEvent : this.m_uiUnitDefinition.getNormalTagEvents())  eleEventsJsons.add(elementEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTEVENTS, HAPJsonUtility.buildArrayJson(eleEventsJsons.toArray(new String[0])));
		
		List<String> tagEvents = new ArrayList<String>();
		for(HAPElementEvent tagEvent : this.m_uiUnitDefinition.getCustomTagEvents())		tagEvents.add(tagEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TAGEVENTS, HAPJsonUtility.buildArrayJson(tagEvents.toArray(new String[0])));
		
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildMapJson(this.m_uiUnitDefinition.getAttributes()));
		
		String htmlContent = StringEscapeUtils.escapeHtml(this.m_uiUnitDefinition.getContent());
		htmlContent = htmlContent.replaceAll("(\\r|\\n)", "");
		jsonMap.put(HTML, htmlContent);
		
		jsonMap.put(CONSTANTS, HAPJsonUtility.buildJson(this.m_scriptExpressionContext.getConstants(), HAPSerializationFormat.JSON));
	
		jsonMap.put(EVENTS, HAPJsonUtility.buildJson(this.m_eventsDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(COMMANDS, HAPJsonUtility.buildJson(this.m_commandsDefinition, HAPSerializationFormat.JSON));

		jsonMap.put(SERVICES, HAPJsonUtility.buildJson(this.m_servicesDefinition, HAPSerializationFormat.JSON));

		
		
		List<String> expressionContentJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInContent expressionContent : this.m_scriptExpressionsInContent)  expressionContentJsons.add(expressionContent.toStringValue(format));
		jsonMap.put(SCRIPTEXPRESSIONSINCONTENT, HAPJsonUtility.buildArrayJson(expressionContentJsons.toArray(new String[0])));
		
		List<String> expressionAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionAttr : this.m_scriptExpressionsInAttribute)  expressionAttributeJsons.add(expressionAttr.toStringValue(format));
		jsonMap.put(SCRIPTEXPRESSIONINATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionAttributeJsons.toArray(new String[0])));

		List<String> expressionTagAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionTagAttr : this.m_scriptExpressionsInTagAttribute)  expressionTagAttributeJsons.add(expressionTagAttr.toStringValue(format));
		jsonMap.put(SCRIPTEXPRESSIONINTAGATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionTagAttributeJsons.toArray(new String[0])));

	
		Map<String, String> uiTagJsons = new LinkedHashMap<String, String>();
		for(String uiId : this.m_uiTags.keySet())	uiTagJsons.put(uiId, this.m_uiTags.get(uiId).toStringValue(format));
		jsonMap.put(UITAGS, HAPJsonUtility.buildMapJson(uiTagJsons));
		
	
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>(); 
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		
		this.buildJsonMap(jsonMap, typeJsonMap);
		
		HAPScript script = this.m_uiUnitDefinition.getScriptBlock();
		if(script!=null){
			jsonMap.put(SCRIPT, script.toStringValue(HAPSerializationFormat.JSON_FULL));
			typeJsonMap.put(SCRIPT, script.getClass());
		}
		
		List<String> expressionContentJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInContent expressionContent : this.m_scriptExpressionsInContent)  expressionContentJsons.add(expressionContent.toResourceData(runtimeInfo).toString());
		jsonMap.put(SCRIPTEXPRESSIONSINCONTENT, HAPJsonUtility.buildArrayJson(expressionContentJsons.toArray(new String[0])));
		
		List<String> expressionAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionAttr : this.m_scriptExpressionsInAttribute)  expressionAttributeJsons.add(expressionAttr.toResourceData(runtimeInfo).toString());
		jsonMap.put(SCRIPTEXPRESSIONINATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionAttributeJsons.toArray(new String[0])));

		List<String> expressionTagAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionTagAttr : this.m_scriptExpressionsInTagAttribute)  expressionTagAttributeJsons.add(expressionTagAttr.toResourceData(runtimeInfo).toString());
		jsonMap.put(SCRIPTEXPRESSIONINTAGATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionTagAttributeJsons.toArray(new String[0])));
		
		
		Map<String, String> uiTagJsons = new LinkedHashMap<String, String>();
		for(String uiId : this.m_uiTags.keySet())	uiTagJsons.put(uiId, this.m_uiTags.get(uiId).toResourceData(runtimeInfo).toString());
		jsonMap.put(UITAGS, HAPJsonUtility.buildMapJson(uiTagJsons));
				
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		
		//resource from expression
		for(HAPUIEmbededScriptExpressionInContent expressionContent : this.m_scriptExpressionsInContent)  out.addAll(expressionContent.getResourceDependency(runtimeInfo));
		for(HAPUIEmbededScriptExpressionInAttribute expressionAttr : this.m_scriptExpressionsInAttribute)  out.addAll(expressionAttr.getResourceDependency(runtimeInfo));
		for(HAPUIEmbededScriptExpressionInAttribute expressionTagAttr : this.m_scriptExpressionsInTagAttribute)  out.addAll(expressionTagAttr.getResourceDependency(runtimeInfo));
		
		//resource from child uiTag
		for(String uiId : this.m_uiTags.keySet()) out.addAll(this.m_uiTags.get(uiId).getResourceDependency(runtimeInfo));	
		
		return out;
	}
}
