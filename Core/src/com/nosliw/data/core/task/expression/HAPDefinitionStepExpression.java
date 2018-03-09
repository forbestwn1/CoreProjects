package com.nosliw.data.core.task.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.operand.HAPOperandOperation;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;

public class HAPDefinitionStepExpression extends HAPDefinitionStep{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	//output variable name
	@HAPAttribute
	public static String OUTPUTVARIABLE = "outputVariable";

	@HAPAttribute
	public static String OPERAND = "operand";

	@HAPAttribute
	public static String VARIABLENAMES = "variableNames";

	@HAPAttribute
	public static String EXIT = "exit";

	@HAPAttribute
	public static String REFERENCENAMES = "referenceNames";

	private String m_expression;
	
	private boolean m_exit = false;
	private String m_outputVariable;
	
	private HAPOperandWrapper m_operand;
	
	public HAPDefinitionStepExpression() {
	}
	
	@Override
	public String getType() {  return HAPConstant.EXPRESSIONTASK_STEPTYPE_EXPRESSION;	}
	
	public String getExpression(){  return this.m_expression;    }

	public boolean isExit() {  return this.m_exit;   }
	
	public String getOutputVariable(){  return this.m_outputVariable;  }

	public HAPOperandWrapper getOperand() {  return this.m_operand;  }
	

	private void process() {
		//parse expression
		this.m_operand = new HAPOperandWrapper(HAPManagerExpression.expressionParser.parseExpression(this.getExpression()));

		this.discoverVariables();
		
		this.discoverReferences();
		
		this.processDefaultAnonomousParmInOperation();
		
	}
	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_expression = jsonObj.optString(EXPRESSION);
		this.m_outputVariable = jsonObj.optString(OUTPUTVARIABLE);
		if(HAPBasicUtility.isStringEmpty(m_outputVariable)) {
			Boolean exit = jsonObj.optBoolean(EXIT);
			if(exit==null)  exit = Boolean.TRUE;
			this.m_exit = exit;
		}
		this.process();
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, this.m_expression);
		jsonMap.put(OUTPUTVARIABLE, this.m_outputVariable);
		jsonMap.put(VARIABLENAMES, HAPJsonUtility.buildJson(this.m_variableNames, HAPSerializationFormat.JSON));
		jsonMap.put(REFERENCENAMES, HAPJsonUtility.buildJson(this.m_referenceNames, HAPSerializationFormat.JSON));
		jsonMap.put(OPERAND, HAPJsonUtility.buildJson(this.m_operand, HAPSerializationFormat.JSON));
	}
	
	public void cloneTo(HAPDefinitionStepExpression taskStep){
		
	}

	public HAPDefinitionStepExpression clone() {
		return null;
	}

}
