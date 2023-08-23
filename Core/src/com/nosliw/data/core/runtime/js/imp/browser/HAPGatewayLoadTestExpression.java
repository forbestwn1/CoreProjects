package com.nosliw.data.core.runtime.js.imp.browser;


import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

@HAPEntityWithAttribute
public class HAPGatewayLoadTestExpression extends HAPGatewayImp{

	@HAPAttribute
	public static final String COMMAND_LOADTESTEXPRESSION = "loadTestExpression";
	@HAPAttribute
	final public static String COMMAND_LOADTESTEXPRESSION_ELEMENT_SUITE = "suite";
	@HAPAttribute
	final public static String COMMAND_LOADTESTEXPRESSION_ELEMENT_EXPRESSIONNAME = "expressionName";
	@HAPAttribute
	final public static String COMMAND_LOADTESTEXPRESSION_ELEMENT_VARIABLES = "variables";
	
	public HAPGatewayLoadTestExpression(){
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_LOADTESTEXPRESSION:
			
//			String suite = parms.optString(COMMAND_LOADTESTEXPRESSION_ELEMENT_SUITE);
//			String expressionName = parms.optString(COMMAND_LOADTESTEXPRESSION_ELEMENT_EXPRESSIONNAME);
//			JSONObject variablesObject = (JSONObject)parms.optJSONObject(COMMAND_LOADTESTEXPRESSION_ELEMENT_VARIABLES);
//			
//			Map<String, HAPDataTypeCriteria> variableCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
//			if(variablesObject!=null){
//				Iterator varNamesIt = variablesObject.keys();
//				while(varNamesIt.hasNext()){
//					String varName = (String)varNamesIt.next();
//					String criteria = variablesObject.optString(varName);
//					HAPDataTypeCriteriaWrapperLiterate criteriaObj = new HAPDataTypeCriteriaWrapperLiterate();
//					criteriaObj.buildObject(criteria, HAPSerializationFormat.LITERATE);
//					variableCriterias.put(varName, criteriaObj);
//				}
//			}
//			
//			Map<String, HAPData> varDatas = ((HAPDefinitionTaskSuiteForTest)this.m_taskManager.getTaskDefinitionSuite(suite)).getVariableData();
//			HAPExpression expression = this.m_taskManager.processExpression(null, expressionName, suite, variableCriterias);
//			HAPResponseGatewayLoadTestExpression response = new HAPResponseGatewayLoadTestExpression(expression, varDatas);
//			out = this.createSuccessWithObject(response);
			break;
		}
		
		return out;
	}

}
