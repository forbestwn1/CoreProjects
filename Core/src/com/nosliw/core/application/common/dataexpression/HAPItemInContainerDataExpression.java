package com.nosliw.core.application.common.dataexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPItemInContainerDataExpression extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATAEXPRESSION = "dataExpression";

	private HAPExpressionData m_dataExpression;
	
	public HAPItemInContainerDataExpression() {}
	
	public HAPItemInContainerDataExpression(HAPExpressionData dataExpression) {
		this.m_dataExpression = dataExpression;
	}
	
	public HAPExpressionData getDataExpression() {    return this.m_dataExpression;     }
	public void setDataExpression(HAPExpressionData expression) {    this.m_dataExpression = expression;      }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATAEXPRESSION, this.m_dataExpression.toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}
}
