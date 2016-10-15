package com.nosliw.uiresource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;

/*
 * class that 
 */
public class HAPUIExpressionContent implements HAPSerializable{

	private String m_uiId;
	
	//a list element for content, 
	//two types of elements in this list: string and uiExpression
	private List<Object> m_contentElements;
	
	private HAPDataTypeManager m_dataTypeMan;

	public  HAPUIExpressionContent(String uiId, List<Object> contentElements, HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
		this.m_uiId = uiId;
		this.m_contentElements = contentElements;
	}
	
	public String getUiId(){return this.m_uiId;}
	public void setUiId(String id){this.m_uiId=id;}

	public Set<HAPUIResourceExpression> getUIExpressions(){
		Set<HAPUIResourceExpression> out = new HashSet<HAPUIResourceExpression>();
		for(Object obj : this.m_contentElements){
			if(obj instanceof HAPUIResourceExpression){
				out.add((HAPUIResourceExpression)obj);
			}
		}
		return out;
	}

	protected HAPDataTypeManager getDataTypeManager(){ return this.m_dataTypeMan; }

	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonTypeMap){
		jsonMap.put(HAPAttributeConstant.UIEXPRESSIONCONTENT_UIID, this.m_uiId);
		jsonMap.put(HAPAttributeConstant.UIEXPRESSIONCONTENT_UIEXPRESSIONELEMENTS, HAPJsonUtility.getListObjectJson(this.m_contentElements));
	}
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class> jsonTypeMap = new LinkedHashMap<String, Class>();
		this.buildJsonMap(jsonMap, jsonTypeMap);
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
