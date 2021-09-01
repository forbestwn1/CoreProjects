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
import com.nosliw.data.core.component.HAPExecutableComponent;
import com.nosliw.data.core.component.command.HAPReferenceCommand;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuiteImp;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.expression.HAPContextProcessExpressionScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPElementEvent;

@HAPEntityWithAttribute
public class HAPExecutableUIBody extends HAPExecutableComponent{

//	@HAPAttribute
//	public static final String VALUESTRUCTURE = "valueStructure";
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
	public static final String HANDLERS = "handlers";
	@HAPAttribute
	public static final String SERVICEPROVIDERS = "serviceProviders";

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
	
	private List<HAPReferenceCommand> m_commandReferences;

	private Set<HAPElementEvent> m_elementEvents;
	private Set<HAPElementEvent> m_tagEvents;

	//all java script blocks within this unit
	private HAPJsonTypeScript m_script;
	
	private String m_html;
	
	public HAPExecutableUIBody(HAPDefinitionUIUnit uiUnitDefinition, HAPExecutableUIUnit uiUnit) {
		super(uiUnitDefinition, null);
		this.m_elementEvents = uiUnitDefinition.getNormalTagEvents();
		this.m_tagEvents = uiUnitDefinition.getCustomTagEvents();
		this.m_script = uiUnitDefinition.getScriptBlock();
		
		this.m_html = StringEscapeUtils.escapeHtml(uiUnitDefinition.getContent()).replaceAll("(\\r|\\n)", "");
		
		this.m_scriptExpressionsInContent = new HashSet<HAPUIEmbededScriptExpressionInContent>();
		this.m_scriptExpressionsInAttribute = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInTagAttribute = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		this.m_processScriptContext = new HAPContextProcessExpressionScript();
		this.m_uiTags = new LinkedHashMap<String, HAPExecutableUIUnitTag>();
//		this.m_constants = new LinkedHashMap<String, Object>();

		//build tag trees according to definition
		for(HAPDefinitionUITag tag : uiUnitDefinition.getUITags()) {
			HAPExecutableUIUnitTag exeTag = new HAPExecutableUIUnitTag(tag, tag.getId());
			this.m_uiTags.put(tag.getId(), exeTag);
			exeTag.setParent(uiUnit);
		}
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
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		
		List<String> eleEventsJsons = new ArrayList<String>();
		for(HAPElementEvent elementEvent : this.m_elementEvents)  eleEventsJsons.add(elementEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTEVENTS, HAPJsonUtility.buildArrayJson(eleEventsJsons.toArray(new String[0])));
		
		List<String> tagEvents = new ArrayList<String>();
		for(HAPElementEvent tagEvent : this.m_tagEvents)		tagEvents.add(tagEvent.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TAGEVENTS, HAPJsonUtility.buildArrayJson(tagEvents.toArray(new String[0])));
		
		jsonMap.put(HTML, this.m_html);
		
//		jsonMap.put(CONSTANTS, HAPJsonUtility.buildJson(this.m_processScriptContext.getConstantsValue(), HAPSerializationFormat.JSON));
	
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
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
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

		jsonMap.put(SCRIPTGROUP, this.m_scriptGroupExe.toResourceData(runtimeInfo).toString());
		
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
