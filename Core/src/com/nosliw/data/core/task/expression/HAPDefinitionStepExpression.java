package com.nosliw.data.core.task.expression;

import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.operand.HAPOperandWrapper;

public class HAPDefinitionStepExpression extends HAPDefinitionStep{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	@HAPAttribute
	public static String EXIT = "exit";

	//output variable name
	@HAPAttribute
	public static String OUTPUTVARIABLE = "outputVariable";

	private HAPDefinitionExpression m_expression;
	
	private boolean m_exit = false;
	private String m_outputVariable;
	
	public HAPDefinitionStepExpression() {
	}
	
	@Override
	public String getType() {  return HAPConstant.EXPRESSIONTASK_STEPTYPE_EXPRESSION;	}
	
	public String getExpression(){  return this.m_expression.getExpression();    }
	private void setExpression(String expression) {	this.m_expression = new HAPDefinitionExpression(expression);	}

	public boolean isExit() {  return this.m_exit;   }
	
	public String getOutputVariable(){  return this.m_outputVariable;  }

	public HAPOperandWrapper getOperand() {  return this.m_expression.getOperand();  }

	@Override
	public Set<String> getVariableNames() {	return this.m_expression.getVariableNames();	}

	@Override
	public Set<String> getReferenceNames() {  return this.m_expression.getReferenceNames(); }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		String expressionStr = jsonObj.optString(EXPRESSION);
		this.setExpression(expressionStr);
		this.m_outputVariable = jsonObj.optString(OUTPUTVARIABLE);
		if(HAPBasicUtility.isStringEmpty(this.m_outputVariable))   this.m_outputVariable = null;
		if(HAPBasicUtility.isStringEmpty(m_outputVariable)) {
			Boolean exit = jsonObj.optBoolean(EXIT);
			if(exit==null)  exit = Boolean.TRUE;
			this.m_exit = exit;
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, HAPJsonUtility.buildJson(this.m_expression, HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTVARIABLE, this.m_outputVariable);
	}
}
