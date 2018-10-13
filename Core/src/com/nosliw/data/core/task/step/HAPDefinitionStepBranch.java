package com.nosliw.data.core.task.step;

import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.task.HAPDefinitionStep;

public class HAPDefinitionStepBranch{
//extends HAPDefinitionStep{
/*
	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	@HAPAttribute
	public static String TRUERESULT = "true";
	
	@HAPAttribute
	public static String FALSERESULT = "false";
	
	private HAPDefinitionExpression m_expression;
	
	private HAPResultStep m_trueResult;

	private HAPResultStep m_falseResult;

	public HAPDefinitionExpression getExpression() {  return this.m_expression;   }
	public HAPResultStep getTrueResult() {   return this.m_trueResult;  }
	public HAPResultStep getFalseResult() {   return this.m_falseResult;  }
	
	@Override
	public String getType() {  return HAPConstant.EXPRESSIONTASK_STEPTYPE_BRANCH;  }

	@Override
	public Set<String> getVariableNames() {   return this.m_expression.getVariableNames(); }

	@Override
	public Set<String> getReferenceNames() {  return this.m_expression.getReferenceNames(); }

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;

			this.m_expression = new HAPDefinitionExpression(jsonObj.getString(EXPRESSION));

			this.m_trueResult = new HAPResultStep();
			this.m_trueResult.buildObjectByJson(jsonObj.getJSONObject(TRUERESULT));
			this.m_falseResult = new HAPResultStep();
			this.m_falseResult.buildObjectByJson(jsonObj.getJSONObject(FALSERESULT));
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	*/
}
