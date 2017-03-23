package com.nosliw.uiresource;

import java.util.List;
import java.util.Map;

import com.nosliw.data.core.HAPDataTypeManager;

public class HAPUIExpressionAttribute extends HAPUIExpressionContent{
	//attribute name
	private String m_attribute;

	public HAPUIExpressionAttribute(String uiId, String attr, List<Object> contentElements, HAPDataTypeManager dataTypeMan){
		super(uiId, contentElements, dataTypeMan);
		this.m_attribute = attr;
	}
	
	public void setAttribute(String attr){this.m_attribute=attr;}
	public String getAttribute(){return this.m_attribute;}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPAttributeConstant.UIEXPRESSIONCONTENT_ATTRIBUTE, this.m_attribute);
	}
}
