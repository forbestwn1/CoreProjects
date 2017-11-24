package com.nosliw.uiresource.definition;

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
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.uiresource.context.HAPContext;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionContext;

/*
 * ui resource basic class for both ui resource and custom tag
 * it contains all the information after ui resource is compiled
 * it contains all the information within its domain
 * 		that means, for ui resource instance, it does not contains infor within customer tag
 */
@HAPEntityWithAttribute(baseName="UIRESOURCEDEFINITION")
public abstract class HAPUIDefinitionUnit extends HAPSerializableImp{

	@HAPAttribute
	public static final String ID = "id";
	@HAPAttribute
	public static final String CONTEXT = "context";
	@HAPAttribute
	public static final String PRIVATECONTEXT = "privateContext";
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
	public static final String SCRIPTFACTORYNAME = "scriptFactoryName";
	@HAPAttribute
	public static final String DATABINDINGS = "dataBindings";
	@HAPAttribute
	public static final String UITAGLIBS = "uiTagLibs";
	@HAPAttribute
	public static final String CONSTANTS = "constants";
	@HAPAttribute
	public static final String EXPRESSIONS = "expressions";
	@HAPAttribute
	public static String EXPRESSIONUNIT = "expressionUnit";

	
	//for tag, it is tag id within resource
	//for resource, it is resource name
	private String m_id;

	//context definition
	private HAPContext m_context;
	
	//private context
	private HAPContext m_privateContext;
	
	//a set of named data that can be used as constants
	private Map<String, HAPConstantDef> m_constantDefs;
	
	//calcualted constant values from constantDefs
	private Map<String, Object> m_constantValues;
	
	//all the expressions within content under this domain
	private Set<HAPEmbededScriptExpressionInContent> m_scriptExpressionsInContent;
	//all the attribute expressions in regular tag under this domain 
	private Set<HAPEmbededScriptExpressionInAttribute> m_scriptExpressionsInAttribute;
	//all the attribute expressions in customer tag under this domain
	private Set<HAPEmbededScriptExpressionInAttribute> m_scriptExpressionsInTagAttribute;
	
	//store all the attribute for this domain
	//for customer tag, they are the tag's attribute
	//for resource, they are the attribute of body
	private Map<String, String> m_attributes;
	
	//all java script blocks within this domain
	private HAPScript m_script;
	
	//html content after compilation
	private String m_content; 

	//all the events related with regular tag
	private Set<HAPElementEvent> m_elementEvents;

	
	
	
	//all the events related with customer tag
	private Set<HAPElementEvent> m_tagEvents;
	
	//all the customer tag within the domain
	private Map<String, HAPUIDefinitionUnitTag> m_uiTags; 
	
	//the script factory name for creating script object for ui resource view
	private String m_scriptFactoryName;
	
	private Map<String, HAPExpressionDefinition> m_expressionDefinitions;
	
	//expression unit
	private HAPUIResourceExpressionContext m_expressionContext;
	
	public HAPUIDefinitionUnit(String id){
		this.m_id = id;
		this.m_context = new HAPContext();
		this.m_privateContext = new HAPContext();
		this.m_scriptExpressionsInAttribute = new HashSet<HAPEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInTagAttribute = new HashSet<HAPEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInContent = new HashSet<HAPEmbededScriptExpressionInContent>();
		this.m_uiTags = new LinkedHashMap<String, HAPUIDefinitionUnitTag>();
		this.m_elementEvents = new HashSet<HAPElementEvent>();
		this.m_tagEvents = new HashSet<HAPElementEvent>();
		this.m_attributes = new LinkedHashMap<String, String>();
		this.m_constantDefs = new LinkedHashMap<String, HAPConstantDef>();
		this.m_constantValues = new LinkedHashMap<String, Object>();
		this.m_expressionDefinitions = new LinkedHashMap<String, HAPExpressionDefinition>();
		this.m_expressionContext = new HAPUIResourceExpressionContext();
	}
	
	abstract public String getType(); 

	public Set<HAPExpressionDefinition> getOtherExpressionDefinitions(){  return new HashSet(this.m_expressionDefinitions.values());	}
	
	@Override
	public String toString(){
		return HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(TYPE, String.valueOf(this.getType()));

		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(m_context, HAPSerializationFormat.JSON_FULL));
		
		List<String> expressionContentJsons = new ArrayList<String>();
		for(HAPEmbededScriptExpressionInContent expressionContent : this.m_scriptExpressionsInContent)  expressionContentJsons.add(expressionContent.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(SCRIPTEXPRESSIONSINCONTENT, HAPJsonUtility.buildArrayJson(expressionContentJsons.toArray(new String[0])));
		
		List<String> expressionAttributeJsons = new ArrayList<String>();
		for(HAPEmbededScriptExpressionInAttribute expressionAttr : this.m_scriptExpressionsInAttribute)  expressionAttributeJsons.add(expressionAttr.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(SCRIPTEXPRESSIONINATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionAttributeJsons.toArray(new String[0])));

		List<String> expressionTagAttributeJsons = new ArrayList<String>();
		for(HAPEmbededScriptExpressionInAttribute expressionTagAttr : this.m_scriptExpressionsInTagAttribute)  expressionTagAttributeJsons.add(expressionTagAttr.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(SCRIPTEXPRESSIONINTAGATTRIBUTES, HAPJsonUtility.buildArrayJson(expressionTagAttributeJsons.toArray(new String[0])));

		if(this.m_script!=null)		jsonMap.put(SCRIPT, this.m_script.getBlock());
		
		
		List<String> eleEventsJsons = new ArrayList<String>();
		for(HAPElementEvent elementEvent : this.m_elementEvents)  eleEventsJsons.add(elementEvent.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(ELEMENTEVENTS, HAPJsonUtility.buildArrayJson(eleEventsJsons.toArray(new String[0])));
		
		List<String> tagEvents = new ArrayList<String>();
		for(HAPElementEvent tagEvent : m_tagEvents)		tagEvents.add(tagEvent.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(TAGEVENTS, HAPJsonUtility.buildArrayJson(tagEvents.toArray(new String[0])));
		
		Map<String, String> uiTagJsons = new LinkedHashMap<String, String>();
		for(String uiId : this.m_uiTags.keySet())	uiTagJsons.put(uiId, this.m_uiTags.get(uiId).toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(UITAGS, HAPJsonUtility.buildMapJson(uiTagJsons));
		
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildMapJson(this.m_attributes));
		
		String htmlContent = StringEscapeUtils.escapeHtml(this.getContent());
		htmlContent = htmlContent.replaceAll("(\\r|\\n)", "");
		jsonMap.put(HTML, htmlContent);
		
		jsonMap.put(SCRIPTFACTORYNAME, this.m_scriptFactoryName);

		Map<String, String> constantsJsons = new LinkedHashMap<String, String>();
		for(String name : this.m_constantDefs.keySet()){
			constantsJsons.put(name, this.m_constantDefs.get(name).toStringValue(HAPSerializationFormat.JSON_FULL));
		}
		jsonMap.put(CONSTANTS, HAPJsonUtility.buildMapJson(constantsJsons));
	
	}
	
	
	public String getId(){return this.m_id;}
	public String getContent(){return this.m_content;}
	public void setContent(String content){	this.m_content = content;	}
	
	public HAPContext getContext(){  return this.m_context;  }
	public HAPContext getPrivateContext(){  return this.m_privateContext;  }
	public void addScriptExpressionInAttribute(HAPEmbededScriptExpressionInAttribute eAttr){	this.m_scriptExpressionsInAttribute.add(eAttr);	}
	public void addScriptExpressionInTagAttribute(HAPEmbededScriptExpressionInAttribute eAttr){	this.m_scriptExpressionsInTagAttribute.add(eAttr);	}
	public void addScriptExpressionInContent(HAPEmbededScriptExpressionInContent scriptExpressionInContent){	this.m_scriptExpressionsInContent.add(scriptExpressionInContent);	}
	public void addUITag(HAPUIDefinitionUnitTag uiTag){	this.m_uiTags.put(uiTag.getId(), uiTag);	}
	public void addElementEvent(HAPElementEvent event){this.m_elementEvents.add(event);}
	public void addTagEvent(HAPElementEvent event){this.m_tagEvents.add(event);}
	public void setJSBlock(HAPScript jsBlock){this.m_script = jsBlock;}
	public void setScriptFactoryName(String name){this.m_scriptFactoryName=name;}
	public void addConstantDef(String name, HAPConstantDef data){this.m_constantDefs.put(name, data);}
	public void setConstantDefs(Map<String, HAPConstantDef> data){this.m_constantDefs = data;}
	public Map<String, HAPConstantDef> getConstantDefs(){return this.m_constantDefs;}
	public void addConstant(String name, Object data){this.m_constantValues.put(name, data);}
	public Map<String, Object> getConstantValues(){return this.m_constantValues;}
	public Map<String, String> getAttributes(){  return this.m_attributes;  }
	
	public HAPScript getJSBlock(){return this.m_script;}
	public Collection<HAPUIDefinitionUnitTag> getUITags(){return this.m_uiTags.values();} 
	public Map<String, HAPUIDefinitionUnitTag> getUITagesByName(){   return this.m_uiTags;   }
	public Set<HAPEmbededScriptExpressionInContent> getScriptExpressionsInContent(){return this.m_scriptExpressionsInContent;}
	public Set<HAPEmbededScriptExpressionInAttribute> getScriptExpressionsInAttributes(){return this.m_scriptExpressionsInAttribute;}
	public Set<HAPEmbededScriptExpressionInAttribute> getScriptExpressionsInTagAttributes(){return this.m_scriptExpressionsInTagAttribute;}

	public HAPUIResourceExpressionContext getExpressionContext(){   return this.m_expressionContext;   }
	public void setExpressionContext(HAPUIResourceExpressionContext context){  this.m_expressionContext = context;   }
	
	public void addExpressionDefinition(HAPExpressionDefinition expressionDef){		this.m_expressionDefinitions.put(expressionDef.getName(), expressionDef);	}
	
	/*
	 * process attributes
	 */
	public void addAttribute(String name, String value){		this.m_attributes.put(name, value);	}

	public void postRead(){
	}
}
