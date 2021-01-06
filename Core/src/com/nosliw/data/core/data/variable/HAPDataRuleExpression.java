package com.nosliw.data.core.data.variable;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPDataRuleExpression extends HAPDataRuleImp{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String EXPRESSIONEXECUTE = "expressionExecute";

	@HAPAttribute
	public static String VARIABLENAME = "data";

	
	private String m_expressionDef;

	private HAPExecutableExpressionGroup m_expressionExe;
	
	public HAPDataRuleExpression() {
		super(HAPConstant.DATARULE_TYPE_EXPRESSION);
	}

	public HAPDataRuleExpression(String expression) {
		this();
		this.m_expressionDef = expression;
	}
	
	public String getExpressionDefinition() {   return this.m_expressionDef;   }
	public void setExpressionDefinition(String expression) {   this.m_expressionDef = expression;    }

	public HAPExecutableExpressionGroup getExpressionExecutable() {    return this.m_expressionExe;     }
	
	@Override
	public HAPServiceData verify(HAPData data, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(HAPDataTypeCriteria criteria, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPDataTypeCriteria> varsCriteria = new LinkedHashMap<String, HAPDataTypeCriteria>(); 
		varsCriteria.put(VARIABLENAME, criteria);
		this.m_expressionExe = runtimeEnv.getExpressionManager().getExpression(this.m_expressionDef, varsCriteria);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, HAPJsonUtility.buildJson(this.getExpressionDefinition(), HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONEXECUTE, HAPJsonUtility.buildJson(this.getExpressionExecutable(), HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObjectByJson(Object value) {
		JSONObject valueObj = (JSONObject)value;
		super.buildObjectByJson(valueObj);
		this.m_expressionDef = valueObj.getString(EXPRESSION);
		return true;
	}

}
