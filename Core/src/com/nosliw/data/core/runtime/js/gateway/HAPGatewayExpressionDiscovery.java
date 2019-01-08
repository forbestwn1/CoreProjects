package com.nosliw.data.core.runtime.js.gateway;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPExecutableExpression;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteExpressionRhino;

public class HAPGatewayExpressionDiscovery extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA = "getOutputCriteria";
	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA_EXPRESSION = "expression";
	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA_CONSTANTS = "constants";
	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA_VARIABLESCRITERIA = "variablesCriteria";

	@HAPAttribute
	final public static String COMMAND_EXECUTEEXPRESSION = "executeExpression";
	@HAPAttribute
	final public static String COMMAND_EXECUTEEXPRESSION_EXPRESSION = "expression";
	@HAPAttribute
	final public static String COMMAND_EXECUTEEXPRESSION_CONSTANTS = "constants";
	@HAPAttribute
	final public static String COMMAND_EXECUTEEXPRESSION_VARIABLESVALUE = "variablesValue";
	
	
	private HAPExpressionSuiteManager m_expressionSuiteManager;
	
	private HAPRuntime m_runtime;
	
	public HAPGatewayExpressionDiscovery(HAPExpressionSuiteManager expressionSuiteManager, HAPRuntime runtime){
		this.m_expressionSuiteManager = expressionSuiteManager;
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_GETOUTPUTCRITERIA:
		{
			HAPDefinitionExpression expressionDef = new HAPDefinitionExpression(parms.getString(COMMAND_GETOUTPUTCRITERIA_EXPRESSION));
			
			Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			JSONObject varCriteriasJson = parms.optJSONObject(COMMAND_GETOUTPUTCRITERIA_VARIABLESCRITERIA);
			Iterator<String> it1 = varCriteriasJson.keys();
			while(it1.hasNext()){
				String varName = it1.next();
				String criteriaStr = varCriteriasJson.getString(varName);
				HAPDataTypeCriteria varCriteria = HAPCriteriaParser.getInstance().parseCriteria(criteriaStr);
				varsInfo.put(varName, HAPVariableInfo.buildVariableInfo(varCriteria));
			}

			JSONObject constantsJson = parms.optJSONObject(COMMAND_GETOUTPUTCRITERIA_CONSTANTS);
			Map<String, HAPData> constants = HAPDataUtility.buildDataWrapperMapFromJson(constantsJson); 
			
			HAPProcessTracker processTracker = new HAPProcessTracker();
			HAPExecutableExpression expression = this.m_expressionSuiteManager.compileExpression(expressionDef, varsInfo, constants, null, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), processTracker);
			HAPDataTypeCriteria outCriteria = expression.getOperand().getOperand().getOutputCriteria();
			out = this.createSuccessWithObject(outCriteria.toStringValue(HAPSerializationFormat.LITERATE));
			break;
		}
		case COMMAND_EXECUTEEXPRESSION:
		{
			HAPDefinitionExpression expressionDef = new HAPDefinitionExpression(parms.getString(COMMAND_EXECUTEEXPRESSION_EXPRESSION));

			JSONObject constantsJson = parms.optJSONObject(COMMAND_EXECUTEEXPRESSION_CONSTANTS);
			Map<String, HAPData> constants = HAPDataUtility.buildDataWrapperMapFromJson(constantsJson); 

			Map<String, HAPData> expressionParms = new LinkedHashMap<String, HAPData>();
			Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			JSONObject varValuesJson = parms.getJSONObject(COMMAND_EXECUTEEXPRESSION_VARIABLESVALUE);
			Iterator<String> it = varValuesJson.keys();
			while(it.hasNext()){
				String varName = it.next();
				JSONObject varDataJson = varValuesJson.getJSONObject(varName);
				HAPDataWrapper data = HAPDataUtility.buildDataWrapperFromJson(varDataJson);
				expressionParms.put(varName, data);
				varsInfo.put(varName, HAPVariableInfo.buildVariableInfo(new HAPDataTypeCriteriaId(data.getDataTypeId(), null)));
			}
			
			HAPProcessTracker processTracker = new HAPProcessTracker();
			HAPExecutableExpression expression = this.m_expressionSuiteManager.compileExpression(expressionDef, varsInfo, constants, null, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), processTracker);
			HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(expression, expressionParms, null);
			HAPServiceData serviceData = this.m_runtime.executeTaskSync(task);
			if(serviceData.isSuccess()){
				out = this.createSuccessWithObject(serviceData.getData());
			}
			break;
		}
		}
		
		return out;
	}

}
