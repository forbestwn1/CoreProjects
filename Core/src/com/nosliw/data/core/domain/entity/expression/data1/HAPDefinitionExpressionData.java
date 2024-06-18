package com.nosliw.data.core.domain.entity.expression.data1;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.application.common.dataexpression.HAPOperand;
import com.nosliw.data.core.application.common.dataexpression.HAPWrapperOperand;

public class HAPDefinitionExpressionData extends HAPEntityInfoWritableImp{

	public static String EXPRESSION = "expression";

	public static String OPERAND = "operand";

	private String m_expression;
	
	private HAPWrapperOperand m_operand;

	public HAPDefinitionExpressionData() {
	}

	public HAPDefinitionExpressionData(String expression) {
		this.m_expression = expression;
	}

	public String getExpression() {   return this.m_expression;    }
	
	public HAPWrapperOperand getOperand() {    return this.m_operand;      }
	public void setOperand(HAPOperand operand) {    this.m_operand = new HAPWrapperOperand(operand);     }
	
	public HAPDefinitionExpressionData cloneDefinitionExpression() {
		HAPDefinitionExpressionData out = new HAPDefinitionExpressionData(this.m_expression);
		out.m_operand = this.m_operand.cloneWrapper();
		this.cloneToEntityInfo(out);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, this.m_expression);
		jsonMap.put(OPERAND, this.m_operand.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_expression = jsonObj.getString(HAPDefinitionExpressionData.EXPRESSION);
		return true;  
	}
}
