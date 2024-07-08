package com.nosliw.core.application.brick.dataexpression.group;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.dataexpression.HAPDataExpression;

@HAPEntityWithAttribute
public class HAPItemInGroupDataExpression extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATAEXPRESSION = "dataExpression";
	
	private HAPDataExpression m_dataExpression;

	public void setDataExpression(HAPDataExpression dataExpression) {     this.m_dataExpression = dataExpression;      }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATAEXPRESSION, this.m_dataExpression.toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}
}
