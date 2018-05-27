package com.nosliw.uiresource.page;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContext;

public class HAPUIDefinitionUnitTag extends HAPUIDefinitionUnit{

	@HAPAttribute
	public static final String TAGNAME = "tagName";
	
	//name of this customer tag
	private String m_tagName;

	//tag context definition
	private HAPUITagDefinitionContext m_contextDefinition;
	
	public HAPUIDefinitionUnitTag(String tagName, String id){
		super(id);
		this.m_tagName = tagName;
	}
	
	public void setContextDefinition(HAPUITagDefinitionContext contextDefinition){		this.m_contextDefinition = contextDefinition;	}

	public HAPUITagDefinitionContext getContextDefinition(){  return this.m_contextDefinition;	}
	
	public String getTagName(){	return this.m_tagName;}
	
	@Override
	public String getType() {
		return HAPConstant.UIRESOURCE_TYPE_TAG;
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, this.m_tagName);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, this.m_tagName);
	}

}
