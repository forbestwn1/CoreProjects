package com.nosliw.data.core.runtime.js.gateway;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPGatewayExpressionDiscovery extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA = "getOutputCriteria";
	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA_EXPRESSION = "expression";
	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA_VARIABLESCRITERIA = "variablesCriteria";

	@HAPAttribute
	final public static String COMMAND_EXECUTEEXPRESSION = "executeExpression";
	@HAPAttribute
	final public static String COMMAND_EXECUTEEXPRESSION_EXPRESSION = "expression";
	@HAPAttribute
	final public static String COMMAND_EXECUTEEXPRESSION_VARIABLESVALUE = "variablesValue";
	
	
	private HAPManagerTask m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	public HAPGatewayExpressionDiscovery(HAPManagerTask expressionManager, HAPRuntime runtime){
		this.m_expressionManager = expressionManager;
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_GETOUTPUTCRITERIA:
		{
			JSONObject expressionDefJSON = parms.getJSONObject(COMMAND_GETOUTPUTCRITERIA_EXPRESSION);
			HAPDefinitionTask expressionDefinition = (HAPDefinitionTask)HAPStringableEntityImporterJSON.parseJsonEntity(expressionDefJSON, "data.expressiondefinition", HAPValueInfoManager.getInstance());
			
			Map<String, HAPDataTypeCriteria> varCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
			JSONObject varCriteriasJson = parms.getJSONObject(COMMAND_GETOUTPUTCRITERIA_VARIABLESCRITERIA);
			Iterator<String> it = varCriteriasJson.keys();
			while(it.hasNext()){
				String varName = it.next();
				String criteriaStr = varCriteriasJson.getString(varName);
				HAPDataTypeCriteria varCriteria = HAPCriteriaParser.getInstance().parseCriteria(criteriaStr);
				varCriterias.put(varName, varCriteria);
			}
//			HAPExpressionDefinition expressionDefinition = this.m_expressionManager.newExpressionDefinition(expressionDef, "", null, varCriterias);
			HAPExpression expression = this.m_expressionManager.processExpression(null, expressionDefinition, new LinkedHashMap<String, HAPData>(), varCriterias, HAPExpressionProcessConfigureUtil.setDoDiscovery(null));
			HAPDataTypeCriteria outCriteria = expression.getOperand().getOperand().getOutputCriteria();
			out = this.createSuccessWithObject(outCriteria.toStringValue(HAPSerializationFormat.LITERATE));
			break;
		}
		case COMMAND_EXECUTEEXPRESSION:
		{
			JSONObject expressionDefJSON = parms.getJSONObject(COMMAND_EXECUTEEXPRESSION_EXPRESSION);
			HAPDefinitionTask expressionDefinition = (HAPDefinitionTask)HAPStringableEntityImporterJSON.parseJsonEntity(expressionDefJSON, "data.expressiondefinition", HAPValueInfoManager.getInstance());

			Map<String, HAPData> expressionParms = new LinkedHashMap<String, HAPData>();
			Map<String, HAPDataTypeCriteria> varCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
			JSONObject varValuesJson = parms.getJSONObject(COMMAND_EXECUTEEXPRESSION_VARIABLESVALUE);
			Iterator<String> it = varValuesJson.keys();
			while(it.hasNext()){
				String varName = it.next();
				JSONObject varDataJson = varValuesJson.getJSONObject(varName);
				HAPDataWrapper data = HAPDataUtility.buildDataWrapperFromJson(varDataJson);
				expressionParms.put(varName, data);
				varCriterias.put(varName, new HAPDataTypeCriteriaId(data.getDataTypeId(), null));
			}
			HAPExpression expression = this.m_expressionManager.processExpression(null, expressionDefinition, new LinkedHashMap<String, HAPData>(), varCriterias, HAPExpressionProcessConfigureUtil.setDoDiscovery(null));
			HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(expression, expressionParms);
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
