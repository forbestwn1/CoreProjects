package com.nosliw.uiresource.definition;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.uiresource.expression.HAPScriptExpression;

public class HAPEmbededScriptExpressionInAttribute extends HAPSerializableImp{

	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	@HAPAttribute
	public static final String UIID = "uiId";
	
	private String m_uiId;
	
	//attribute name
	private String m_attribute;

	//a list of elements
	//   string 
	//   script expression
	private List<Object> m_elements;
	
	
	public HAPEmbededScriptExpressionInAttribute(String attr, String uiId, List<Object> elements){
		this.m_attribute = attr;
		this.m_uiId = uiId;
		this.m_elements = elements;
	}
	
	public void setAttribute(String attr){this.m_attribute=attr;}
	public String getAttribute(){return this.m_attribute;}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ATTRIBUTE, this.m_attribute);
		jsonMap.put(UIID, this.m_uiId);
	}
}
