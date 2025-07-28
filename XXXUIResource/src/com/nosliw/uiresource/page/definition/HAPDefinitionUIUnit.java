package com.nosliw.uiresource.page.definition;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPDefinitionEntityComponentImp;

/*
 * ui resource basic class for both ui resource and custom tag
 * it contains all the information after ui resource is compiled
 * it contains all the information within its domain
 * 		that means, for ui resource instance, it does not contains infor within customer tag
 */
public class HAPDefinitionUIUnit extends HAPDefinitionEntityComponentImp{

	//core html content after compilation
	private String m_content; 

	//all the customer tag within the domain
	private Map<String, HAPDefinitionUITag> m_uiTags; 

	//all the events related with regular tag
	private Set<HAPElementEvent> m_normalTagEvents;

	//all java script blocks within this unit
	private HAPJsonTypeScript m_script;

	private HAPDefinitionStyle m_style;
	
	//all the expressions within content under this domain
	private Set<HAPDefinitionUIEmbededScriptExpressionInContent> m_scriptExpressionsInContent;
	//all the attribute expressions in regular tag under this domain 
	private Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInAttribute;
	//all the attribute expressions in customer tag under this domain
	private Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> m_scriptExpressionsInTagAttribute;
	
	public HAPDefinitionUIUnit(String id){
		super(id);
		this.m_scriptExpressionsInAttribute = new HashSet<HAPDefinitionUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInTagAttribute = new HashSet<HAPDefinitionUIEmbededScriptExpressionInAttribute>();
		this.m_scriptExpressionsInContent = new HashSet<HAPDefinitionUIEmbededScriptExpressionInContent>();
		this.m_uiTags = new LinkedHashMap<String, HAPDefinitionUITag>();
		this.m_normalTagEvents = new HashSet<HAPElementEvent>();
//		this.initValueStructure();
	}
	
//	private void initValueStructure() {
//		HAPValueStructureDefinitionGroup valueStructure = new HAPValueStructureDefinitionGroup();
//		//ui error context element
//		HAPRootStructure uiValidationErrorRoot = new HAPRootStructure(new HAPElementStructureLeafValue());
//		uiValidationErrorRoot.setDefaultValue(new JSONObject());
//		uiValidationErrorRoot.setName(HAPConstantShared.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR);
//		valueStructure.addProtectedElement(uiValidationErrorRoot);
//		this.setValueStructure(valueStructure);
//	}
	
	abstract public String getType(); 

	public HAPJsonTypeScript getScriptBlock() {  return this.m_script;  }
	public Set<HAPElementEvent> getNormalTagEvents(){  return this.m_normalTagEvents;   }
	public String getContent() {  return this.m_content;  }
	public HAPDefinitionStyle getStyle() {    return this.m_style;    }
	public void setStyle(HAPDefinitionStyle style) {   this.m_style = style;   }
	
	public void setJSBlock(HAPJsonTypeScript jsBlock){this.m_script = jsBlock;}
	public void setContent(String content){	this.m_content = content;	}
	public Collection<HAPDefinitionUITag> getUITags(){return this.m_uiTags.values();} 

	public Set<HAPDefinitionUIEmbededScriptExpressionInContent> getScriptExpressionsInContent(){   return this.m_scriptExpressionsInContent;   }
	public Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> getScriptExpressionsInAttribute(){   return this.m_scriptExpressionsInAttribute;    }
	public Set<HAPDefinitionUIEmbededScriptExpressionInAttribute> getScriptExpressionsInTagAttribute(){   return this.m_scriptExpressionsInTagAttribute;   }
	
//	public void addExpressionDefinition(String name, String expressionDef){		this.m_expressionDefinitions.put(name, expressionDef);	}
	public void addScriptExpressionInAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute eAttr){	this.m_scriptExpressionsInAttribute.add(eAttr);	}
	public void addScriptExpressionInTagAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute eAttr){	this.m_scriptExpressionsInTagAttribute.add(eAttr);	}
	public void addScriptExpressionInContent(HAPDefinitionUIEmbededScriptExpressionInContent scriptExpressionInContent){	this.m_scriptExpressionsInContent.add(scriptExpressionInContent);	}
	public void addUITag(HAPDefinitionUITag uiTag){		this.m_uiTags.put(uiTag.getId(), uiTag);	}
	public void addElementEvent(HAPElementEvent event){this.m_normalTagEvents.add(event);}
	
	public void postRead(){}
	
	@Override
	public String getValueStructureTypeIfNotDefined() {  return HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP;  }

}
