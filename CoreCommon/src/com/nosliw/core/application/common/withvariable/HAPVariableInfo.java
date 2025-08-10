package com.nosliw.core.application.common.withvariable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.valueport.HAPIdElement;

@HAPEntityWithAttribute
public class HAPVariableInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String VARIABLEKEY = "variableKey";

	@HAPAttribute
	public static String VARIABLEID = "variableId";

	private String m_variableKey;
	
	private HAPIdElement m_variableId;
	
	public HAPVariableInfo(String variableKey, HAPIdElement variableId) {
		this.m_variableKey = variableKey;
		this.m_variableId = variableId;
	}
	
	public String getVariableKey() {    return this.m_variableKey;      }
	
	public HAPIdElement getVariableId() {    return this.m_variableId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VARIABLEKEY, m_variableKey);
		jsonMap.put(VARIABLEID, this.m_variableId.toStringValue(HAPSerializationFormat.JSON));
	}
}
