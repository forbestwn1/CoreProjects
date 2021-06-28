package com.nosliw.data.core.activity.plugin.temp;

public class HAPSwitchActivityDefinition{
//extends HAPDefinitionActivityBranch{
//
//	@HAPAttribute
//	public static String EXPRESSION = "expression";
//	
//	private HAPDefinitionScriptExpression m_expression;
//	
//	public HAPSwitchActivityDefinition(String type) {
//		super(type);
//	}
//	
//	public HAPDefinitionScriptExpression getExpression(){  return this.m_expression;    }
//	private void setExpression(String expression) {	this.m_expression = new HAPDefinitionScriptExpression(expression);	}
//
//	@Override
//	protected boolean buildObjectByJson(Object json){
//		super.buildObjectByJson(json);
//		JSONObject jsonObj = (JSONObject)json;
//		this.buildDefaultInputMapping();
//		String expressionStr = jsonObj.optString(EXPRESSION);
//		this.setExpression(expressionStr);
//		return true;  
//	}
//
//	@Override
//	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
//		super.buildJsonMap(jsonMap, typeJsonMap);
//		jsonMap.put(EXPRESSION, HAPJsonUtility.buildJson(this.m_expression, HAPSerializationFormat.JSON));
//	}
//	
//	@Override
//	public HAPDefinitionActivity cloneActivityDefinition() {
//		HAPSwitchActivityDefinition out = new HAPSwitchActivityDefinition(this.getType());
//		this.cloneToBranchActivityDefinition(out);
//		out.m_expression = this.m_expression.cloneScriptExpressionDefinition();
//		return out;
//	}
}
