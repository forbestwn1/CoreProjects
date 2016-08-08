package com.nosliw.uiresource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;

/*
 * ui resource basic class for both ui resource and custom tag
 * it contains all the information after ui resource is compiled
 * it contains all the information within its domain
 * 		that means, for ui resource instance, it does not contains infor within customer tag
 */
public abstract class HAPUIResourceBasic  implements HAPStringable{

	//for tag, it is tag id within resource
	//for resource, it is resource name
	private String m_id;
	
	//input data context information
	private Map<String, HAPUIResourceContextInfo> m_contextInfos;
	
	//all the expressions within content under this domain
	private Set<HAPUIExpressionContent> m_expressionContents;
	//all the attribute expressions in regular tag under this domain 
	private Set<HAPUIExpressionAttribute> m_expressionAttributes;
	//all the attribute expressions in customer tag under this domain
	private Set<HAPUIExpressionAttribute> m_expressionTagAttributes;
	
	//all the events related with regular tag
	private Set<HAPElementEvent> m_elementEvents;
	//all the events related with customer tag
	private Set<HAPElementEvent> m_tagEvents;
	
	//store all the attribute for this domain
	//for customer tag, they are the tag's attribute
	//for resource, they are the attribute of body
	private Map<String, String> m_attributes;
	
	//all java script blocks within this domain
	private List<HAPJSBlock> m_jsBlocks;
	
	//a set of named data that can be used as constants
	private Map<String, HAPData> m_constants;
	
	//all the customer tag within the domain
	private Map<String, HAPUITag> m_uiTags; 
	
	//html content after compilation
	private String m_content; 

	//the script factory name for creating script object for ui resource view
	private String m_scriptFactoryName;
	
	public HAPUIResourceBasic(String id){
		this.m_id = id;
		this.m_contextInfos = new LinkedHashMap<String, HAPUIResourceContextInfo>();
		this.m_expressionAttributes = new HashSet<HAPUIExpressionAttribute>();
		this.m_expressionTagAttributes = new HashSet<HAPUIExpressionAttribute>();
		this.m_expressionContents = new HashSet<HAPUIExpressionContent>();
		this.m_uiTags = new LinkedHashMap<String, HAPUITag>();
		this.m_jsBlocks = new ArrayList<HAPJSBlock>();
		this.m_elementEvents = new HashSet<HAPElementEvent>();
		this.m_tagEvents = new HashSet<HAPElementEvent>();
		this.m_attributes = new LinkedHashMap<String, String>();
		this.m_constants = new LinkedHashMap<String, HAPData>();
	}
	
	abstract public String getType(); 
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		this.buildBasicJsonMap(jsonMap, format);
		return HAPJsonUtility.getMapJson(jsonMap);
	}
	
	@Override
	public String toString(){
		return HAPJsonUtility.formatJson(this.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
	}

	protected void buildBasicJsonMap(Map<String, String> jsonMap, String format){
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_ID, this.m_id);
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_TYPE, String.valueOf(this.getType()));
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_CONTEXTINFOS, HAPJsonUtility.getMapObjectJson(m_contextInfos));

		List<String> expressionContentJsons = new ArrayList<String>();
		for(HAPUIExpressionContent expressionContent : this.m_expressionContents)  expressionContentJsons.add(expressionContent.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_EXPRESSIONCONTENTS, HAPJsonUtility.getArrayJson(expressionContentJsons.toArray(new String[0])));
		
		List<String> expressionAttributeJsons = new ArrayList<String>();
		for(HAPUIExpressionAttribute expressionAttr : this.m_expressionAttributes)  expressionAttributeJsons.add(expressionAttr.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_EXPRESSIONATTRIBUTES, HAPJsonUtility.getArrayJson(expressionAttributeJsons.toArray(new String[0])));

		List<String> expressionTagAttributeJsons = new ArrayList<String>();
		for(HAPUIExpressionAttribute expressionTagAttr : this.m_expressionTagAttributes)  expressionTagAttributeJsons.add(expressionTagAttr.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_EXPRESSIONTAGATTRIBUTES, HAPJsonUtility.getArrayJson(expressionTagAttributeJsons.toArray(new String[0])));

		List<String> eleEventsJsons = new ArrayList<String>();
		for(HAPElementEvent elementEvent : this.m_elementEvents)  eleEventsJsons.add(elementEvent.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_ELEMENTEVENTS, HAPJsonUtility.getArrayJson(eleEventsJsons.toArray(new String[0])));
		
		List<String> tagEvents = new ArrayList<String>();
		for(HAPElementEvent tagEvent : m_tagEvents)		tagEvents.add(tagEvent.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_TAGEVENTS, HAPJsonUtility.getArrayJson(tagEvents.toArray(new String[0])));
		
		Map<String, String> uiTagJsons = new LinkedHashMap<String, String>();
		for(String uiId : this.m_uiTags.keySet())	uiTagJsons.put(uiId, this.m_uiTags.get(uiId).toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_UITAGS, HAPJsonUtility.getMapJson(uiTagJsons));
		
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_ATTRIBUTES, HAPJsonUtility.getMapJson(this.m_attributes));
		
		String htmlContent = StringEscapeUtils.escapeHtml4(this.getContent());
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_HTML, htmlContent);
		
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_SCRIPTFACTORYNAME, this.m_scriptFactoryName);

		Map<String, String> constantsJsons = new LinkedHashMap<String, String>();
		for(String name : this.m_constants.keySet()){
			constantsJsons.put(name, this.m_constants.get(name).toStringValue(HAPConstant.CONS_SERIALIZATION_JSON_DATATYPE));
		}
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_CONSTANT, HAPJsonUtility.getMapJson(constantsJsons));
	
	}
	
	
	public String getId(){return this.m_id;}
	public String getContent(){return this.m_content;}
	public void setContent(String content){	this.m_content = content;	}
	
	public void addContextInfo(HAPUIResourceContextInfo contextInfo){this.m_contextInfos.put(contextInfo.getName(), contextInfo);	}
	public void addExpressionAttribute(HAPUIExpressionAttribute eAttr){	this.m_expressionAttributes.add(eAttr);	}
	public void addExpressionTagAttribute(HAPUIExpressionAttribute eAttr){	this.m_expressionTagAttributes.add(eAttr);	}
	public void addExpressionContent(HAPUIExpressionContent expressionContent){	this.m_expressionContents.add(expressionContent);	}
	public void addUITag(HAPUITag uiTag){	this.m_uiTags.put(uiTag.getId(), uiTag);	}
	public void addElementEvent(HAPElementEvent event){this.m_elementEvents.add(event);}
	public void addTagEvent(HAPElementEvent event){this.m_tagEvents.add(event);}
	public void addJSBlock(HAPJSBlock jsBlock){this.m_jsBlocks.add(jsBlock);}
	public void setScriptFactoryName(String name){this.m_scriptFactoryName=name;}
	public void addConstant(String name, HAPData data){this.m_constants.put(name, data);}
	public Map<String, HAPData> getConstants(){return this.m_constants;}
	
	public List<HAPJSBlock> getJSBlocks(){return this.m_jsBlocks;}
	public Collection<HAPUITag> getUITags(){return this.m_uiTags.values();} 
	public Set<HAPUIExpressionContent> getExpressionContents(){return this.m_expressionContents;}
	public Set<HAPUIExpressionAttribute> getExpressionAttributes(){return this.m_expressionAttributes;}
	public Set<HAPUIExpressionAttribute> getExpressionTagAttributes(){return this.m_expressionTagAttributes;}

	/*
	 * process attributes
	 */
	public void addAttribute(String name, String value){
		this.m_attributes.put(name, value);
	}

	public void postRead(){
	}
}
