package com.nosliw.data.core.runtime.js.gateway;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

public class HAPGatewayExpressionDiscovery extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA = "getOutputCriteria";
	
	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA_EXPRESSION = "expression";
	
	@HAPAttribute
	final public static String COMMAND_GETOUTPUTCRITERIA_VARIABLESCRITERIA = "variablesCriteria";
	
	private HAPExpressionManager m_expressionManager;
	
	public HAPGatewayExpressionDiscovery(HAPExpressionManager expressionManager){
		this.m_expressionManager = expressionManager;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_GETOUTPUTCRITERIA:
			String expressionDef = parms.getString(COMMAND_GETOUTPUTCRITERIA_EXPRESSION);
			Map<String, HAPDataTypeCriteria> varCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
			JSONObject varCriteriasJson = parms.getJSONObject(COMMAND_GETOUTPUTCRITERIA_VARIABLESCRITERIA);
			Iterator<String> it = varCriteriasJson.keys();
			while(it.hasNext()){
				String varName = it.next();
				String criteriaStr = varCriteriasJson.getString(varName);
				HAPDataTypeCriteria varCriteria = HAPCriteriaParser.getInstance().parseCriteria(criteriaStr);
				varCriterias.put(varName, varCriteria);
			}
			HAPExpressionDefinition expressionDefinition = this.m_expressionManager.newExpressionDefinition(expressionDef, "", null, varCriterias);
			HAPExpression expression = this.m_expressionManager.processExpression(null, expressionDefinition, null, null, null);
			HAPDataTypeCriteria outCriteria = expression.getOperand().getOperand().getOutputCriteria();
			out = HAPServiceData.createSuccessData(outCriteria.toStringValue(HAPSerializationFormat.LITERATE));
			break;
		}
		
		return out;
	}

}
