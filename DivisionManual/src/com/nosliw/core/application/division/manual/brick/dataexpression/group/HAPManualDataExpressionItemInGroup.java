package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.application.common.dataexpression.HAPElementInContainerDataExpression;

public class HAPManualDataExpressionItemInGroup extends HAPEntityInfoImp{

	private String m_expression;
	
	public String getExpression() {    return this.m_expression;     }
	public void setExpression(String expression) {    this.m_expression = expression;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_expression = jsonObj.getString(HAPElementInContainerDataExpression.EXPRESSION);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPElementInContainerDataExpression.EXPRESSION, m_expression);
	}

}
