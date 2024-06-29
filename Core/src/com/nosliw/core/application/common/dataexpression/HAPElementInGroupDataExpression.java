package com.nosliw.core.application.common.dataexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPElementInGroupDataExpression extends HAPEntityInfoImp{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	private HAPDataExpression m_dataExpression;
	
	public HAPElementInGroupDataExpression() {}
	
	public HAPElementInGroupDataExpression(HAPDataExpression dataExpression) {
		this.m_dataExpression = dataExpression;
	}
	
	public HAPDataExpression getExpression() {    return this.m_dataExpression;     }
	public void setExpression(HAPDataExpression expression) {    this.m_dataExpression = expression;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, HAPManagerSerialize.getInstance().toStringValue(this.m_dataExpression, HAPSerializationFormat.JSON));
	}
}
