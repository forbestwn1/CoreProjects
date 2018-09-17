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
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextEntity;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.expressionscript.HAPContextScriptExpressionProcess;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.definition.HAPElementEvent;

@HAPEntityWithAttribute(baseName="UIRESOURCEDEFINITION")
public class HAPExecutableUIUnit extends HAPSerializableImp{

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
	
	private HAPDefinitionUIUnit m_uiUnitDefinition;

	protected HAPExecutableUIUnit m_parent;
	
	//context for content
	private HAPContextGroupInUIResource m_context;
	private HAPContextFlat m_flatContext;
	
	private Map<String, Object> m_constants;
	
	//store all the constant attribute for this domain
	//for customer tag, they are the tag's attribute
	//for resource, they are the attribute of body
	private Map<String, String> m_attributes;
	
	//expression unit
	private HAPContextScriptExpressionProcess m_expressionContext;
	
	//all the customer tag within the domain
	private Map<String, HAPExecutableUIUnitTag> m_uiTags; 
	
	//all the expressions within content under this domain
	private Set<HAPUIEmbededScriptExpressionInContent> m_scriptExpressionsInContent;
	//all the attribute expressions in regular tag under this domain 
	private Set<HAPUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInAttribute;
	//all the attribute expressions in customer tag under this domain
	private Set<HAPUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInTagAttribute;

	//event definition 
	private Map<String, HAPContextEntity> m_eventsDefinition;
	//service requirment definition
	private Map<String, HAPContextEntity> m_servicesDefinition;
	
	public HAPExecutableUIUnit(HAPDefinitionUIUnit uiUnitDefinition) {
		this.m_uiUnitDefinition = uiUnitDefinition;
		this.m_scriptExpressionsInContent = new HashSet<HAPUIEmbededScriptExpressionInContent>();
		this.m_scriptExpressionsInAttribute = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInTagAttribute = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		this.m_expressionContext = new HAPContextScriptExpressionProcess();
		this.m_uiTags = new LinkedHashMap<String, HAPExecutableUIUnitTag>();
		this.m_context = new HAPContextGroupInUIResource(this);
		this.m_attributes = new LinkedHashMap<String, String>();
		this.m_constants = new LinkedHashMap<String, Object>();
		
		//build tag trees according to definition
		for(HAPDefinitionUIUnitTag tag : uiUnitDefinition.getUITags()) {
			HAPExecutableUIUnitTag exeTag = new HAPExecutableUIUnitTag(tag);
			this.m_uiTags.put(tag.getId(), exeTag);
			exeTag.setParent(this);
		}
	}
	
	public String getType() {  return this.m_uiUnitDefinition.getType();  }

	public HAPExecutableUIUnit getParent() {  return this.m_parent;   }
	public void setParent(HAPExecutableUIUnit parent) {		this.m_parent = parent;	}
	public HAPContextGroup getContext(){  return this.m_context;   }
	public void setContext(HAPContextGroup context) {
		this.m_context.clear();
		this.m_context = new HAPContextGroupInUIResource(this, context);
	}
	public HAPContextFlat getFlatContext() { return this.m_flatContext;  }
	public void setFlatContext(HAPContextFlat context) {  this.m_flatContext = context;   }
	public HAPContextFlat getVariableContext() {  return this.m_flatContext.getVariableContext();  }
	
	public Map<String, Object> getConstantsValue(){   return this.m_constants;    }
	public void addConstantValue(String name, Object value) {
		this.m_constants.put(name, value);
		if(value instanceof HAPData) {
			this.m_expressionContext.addConstant(name, (HAPData)value);
		}
	}

	public Map<String, String> getAttributes(){   return this.m_attributes;    }
	public void addAttribute(String name, String value) {   this.m_attributes.put(name, value);   }

	public HAPDefinitionUIUnit getUIUnitDefinition() {	return this.m_uiUnitDefinition;	}
	
	public HAPContextScriptExpressionProcess getExpressionContext(){   return this.m_expressionContext;   }
	public void setExpressionContext(HAPContextScriptExpressionProcess context){  this.m_expressionContext = context;   }

	public Collection<HAPExecutableUIUnitTag> getUITags(){return this.m_uiTags.values();} 
	public HAPExecutableUIUnitTag getUITag(String id){return this.m_uiTags.get(id);} 
	public Map<String, HAPExecutableUIUnitTag> getUITagesByName(){   return this.m_uiTags;   }
	
	public void addScriptExpressionsInContent(HAPUIEmbededScriptExpressionInContent se) {this.m_scriptExpressionsInContent.add(se);}
	public void addScriptExpressionsInAttribute(HAPUIEmbededScriptExpressionInAttribute se) { this.m_scriptExpressionsInAttribute.add(se);   }
	public void addScriptExpressionsInTagAttribute(HAPUIEmbededScriptExpressionInAttribute se) { this.m_scriptExpressionsInTagAttribute.add(se);   }

	public Set<HAPUIEmbededScriptExpressionInContent> getScriptExpressionsInContent() {  return this.m_scriptExpressionsInContent;  }
	public Set<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionsInAttribute() {  return this.m_scriptExpressionsInAttribute;   }
	public Set<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionsInTagAttribute() {  return this.m_scriptExpressionsInTagAttribute;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildMyJsonMap(jsonMap, typeJsonMap, HAPSerializationFormat.JSON);
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildMyJsonMap(jsonMap, typeJsonMap, HAPSerializationFormat.JSON_FULL);
		HAPScript script = this.m_uiUnitDefinition.getScriptBlock();
		if(script!=null){
			jsonMap.put(SCRIPT, script.toStringValue(HAPSerializationFormat.JSON_FULL));
			typeJsonMap.put(SCRIPT, script.getClass());
		}
	}
	
	protected void buildMyJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPSerializationFormat format){
		jsonMap.put(ID, this.m_uiUnitDefinition.getId());
		jsonMap.put(TYPE, String.valueOf(this.getType()));

		jsonMap.put(CONTEXT, this.getVariableContext().toStringValue(format));
		
		List<String> expressionContentJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInContent expressionContent : this.m_scriptExpressionsInContent)  expressionContentJsons.add(expressionContent.toStringValue(format));
		jsonMap.put(SCRIPTEXPRESSIONSINCONTENT, HAPJsonUtility.buildArrayJson(expressionContentJsons.toArray(new String[0])));
		
		List<String> expressionAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionAttr : this.m_scriptExpressionsInAttribute)  expressionAttributeJsons.add(expressionAttr.toStringValue(format));
		jsonMap.put(SCRIPTEXPRESSIONINATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionAttributeJsons.toArray(new String[0])));

		List<String> expressionTagAttributeJsons = new ArrayList<String>();
		for(HAPUIEmbededScriptExpressionInAttribute expressionTagAttr : this.m_scriptExpressionsInTagAttribute)  expressionTagAttributeJsons.add(expressionTagAttr.toStringValue(format));
		jsonMap.put(SCRIPTEXPRESSIONINTAGATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionTagAttributeJsons.toArray(new String[0])));

		
		List<String> eleEventsJsons = new ArrayList<String>();
		for(HAPElementEvent elementEvent : this.m_uiUnitDefinition.getNormalTagEvents())  eleEventsJsons.add(elementEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTEVENTS, HAPJsonUtility.buildArrayJson(eleEventsJsons.toArray(new String[0])));
		
		List<String> tagEvents = new ArrayList<String>();
		for(HAPElementEvent tagEvent : this.m_uiUnitDefinition.getCustomTagEvents())		tagEvents.add(tagEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TAGEVENTS, HAPJsonUtility.buildArrayJson(tagEvents.toArray(new String[0])));
		
		Map<String, String> uiTagJsons = new LinkedHashMap<String, String>();
		for(String uiId : this.m_uiTags.keySet())	uiTagJsons.put(uiId, this.m_uiTags.get(uiId).toStringValue(format));
		jsonMap.put(UITAGS, HAPJsonUtility.buildMapJson(uiTagJsons));
		
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildMapJson(this.m_uiUnitDefinition.getAttributes()));
		
		String htmlContent = StringEscapeUtils.escapeHtml(this.m_uiUnitDefinition.getContent());
		htmlContent = htmlContent.replaceAll("(\\r|\\n)", "");
		jsonMap.put(HTML, htmlContent);
		
		jsonMap.put(CONSTANTS, HAPJsonUtility.buildJson(this.m_constants, HAPSerializationFormat.JSON));
	
		jsonMap.put(EVENTS, HAPJsonUtility.buildJson(this.m_eventsDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICES, HAPJsonUtility.buildJson(this.m_servicesDefinition, HAPSerializationFormat.JSON));
	}

}
