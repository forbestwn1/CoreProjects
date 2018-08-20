package com.nosliw.uiresource.page.definition;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;

public class HAPUIDefinitionUnitTag1 extends HAPDefinitionUIUnit{

	@HAPAttribute
	public static final String TAGNAME = "tagName";
	
	//name of this customer tag
	private String m_tagName;

	public HAPUIDefinitionUnitTag1(String tagName, String id){
		super(id);
		this.m_tagName = tagName;
	}
	
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
