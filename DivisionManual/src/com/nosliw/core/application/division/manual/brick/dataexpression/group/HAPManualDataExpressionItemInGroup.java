package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.application.common.dataexpression.HAPElementInGroupDataExpression;

public class HAPManualDataExpressionItemInGroup extends HAPEntityInfoImp{

	private String m_expression;
	
	public String getExpression() {    return this.m_expression;     }
	public void setExpression(String expression) {    this.m_expression = expression;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_expression = jsonObj.getString(HAPElementInGroupDataExpression.EXPRESSION);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPElementInGroupDataExpression.EXPRESSION, m_expression);
	}

}
