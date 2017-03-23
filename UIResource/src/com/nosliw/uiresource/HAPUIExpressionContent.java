package com.nosliw.uiresource;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeManager;

/*
 * class that 
 */
public class HAPUIExpressionContent extends HAPSerializableImp{

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

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(HAPAttributeConstant.UIEXPRESSIONCONTENT_UIID, this.m_uiId);
		jsonMap.put(HAPAttributeConstant.UIEXPRESSIONCONTENT_UIEXPRESSIONELEMENTS, HAPJsonUtility.buildJson(this.m_contentElements, HAPSerializationFormat.JSON_FULL));
	}
}
