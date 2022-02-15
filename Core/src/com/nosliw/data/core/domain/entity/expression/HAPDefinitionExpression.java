package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

public class HAPDefinitionExpression extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	private String m_expression;
	
	public HAPDefinitionExpression() {
	}

	public HAPDefinitionExpression(String expression) {
		this();
		this.m_expression = expression;
	}

	public String getExpression() {   return this.m_expression;    }
	
	public HAPDefinitionExpression cloneDefinitionExpression() {
		HAPDefinitionExpression out = new HAPDefinitionExpression(this.m_expression);
		this.cloneToEntityInfo(out);
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_expression = jsonObj.getString(HAPDefinitionExpression.EXPRESSION);
		return true;  
	}
}
