package com.nosliw.core.application.brick.dataexpression.group;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

@HAPEntityWithAttribute
public class HAPItemInGroupDataExpression extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATAEXPRESSIONID = "dataExpressionId";
	
	private String m_dataExpressionId;
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATAEXPRESSIONID, this.m_dataExpressionId);
	}
}
