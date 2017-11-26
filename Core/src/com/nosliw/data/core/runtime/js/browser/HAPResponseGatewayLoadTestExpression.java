package com.nosliw.data.core.runtime.js.browser;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;

@HAPEntityWithAttribute
public class HAPResponseGatewayLoadTestExpression extends HAPSerializableImp{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String VARIABLESVALUE = "variablesValue";

	private HAPExpression m_expression;
	
	private Map<String, HAPData> m_variablesValue;
	
	public HAPResponseGatewayLoadTestExpression(HAPExpression expression, Map<String, HAPData> variablesValue){
		this.m_expression = expression;
		this.m_variablesValue = variablesValue; 
	}
	
	public Map<String, HAPData> getVariablesValue(){  return this.m_variablesValue;  }

	public HAPExpression getExpression(){return this.m_expression;}

	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(EXPRESSION, HAPSerializeManager.getInstance().toStringValue(this.m_expression, HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLESVALUE, HAPSerializeManager.getInstance().toStringValue(this.m_variablesValue, HAPSerializationFormat.JSON));
	}
	
}
