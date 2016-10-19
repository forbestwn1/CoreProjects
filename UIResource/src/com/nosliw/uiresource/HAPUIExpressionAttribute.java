package com.nosliw.uiresource;

import java.util.List;
import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data1.HAPDataTypeManager;


public class HAPUIExpressionAttribute extends HAPUIExpressionContent{
	//attribute name
	private String m_attribute;

	public HAPUIExpressionAttribute(String uiId, String attr, List<Object> contentElements, HAPDataTypeManager dataTypeMan){
		super(uiId, contentElements, dataTypeMan);
		this.m_attribute = attr;
	}
	
	public void setAttribute(String attr){this.m_attribute=attr;}
	public String getAttribute(){return this.m_attribute;}

	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonTypeMap){
		super.buildJsonMap(jsonMap, jsonTypeMap);
		jsonMap.put(HAPAttributeConstant.UIEXPRESSIONCONTENT_ATTRIBUTE, this.m_attribute	);
	}
}
