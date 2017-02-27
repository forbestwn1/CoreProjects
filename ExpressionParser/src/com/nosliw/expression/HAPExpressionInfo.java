package com.nosliw.expression;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeCriteria;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataUtility;
import com.nosliw.expression.utils.HAPAttributeConstant;

/*
 * store information related with expression
 * 		expression string
 * 		constantDatas : the constant data to put into expression
 * 		variableInfos : data type information for variables in expression 
 */
public class HAPExpressionInfo extends HAPSerializableImp{

	//expression string
	private String m_expression;
	
	//constants value info
	private Map<String, HAPData> m_constants;
	
	//variable data type info
	private Map<String, HAPDataTypeCriteria> m_variables;
	
	public HAPExpressionInfo(String expression, Map<String, HAPData> constants, Map<String, HAPDataTypeCriteria> variables){
		this.m_expression = expression;
		
		if(constants==null)   this.m_constants = new LinkedHashMap<String, HAPData>();
		else   this.m_constants = constants;
		
		if(variables==null)   this.m_variables = new LinkedHashMap<String, HAPDataTypeCriteria>();
		else this.m_variables = variables;
	}

	public HAPExpressionInfo addVariable(Map<String, HAPDataTypeCriteria> m_variableInfos){
		this.m_variables.putAll(m_variableInfos);
		return this;
	}
	
	public String getExpression(){ return this.m_expression; }
	public Map<String, HAPData> getConstants(){return this.m_constants;}
	public Map<String, HAPDataTypeCriteria> getVariableInfos(){ return this.m_variables; }

	@Override
	protected void buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_expression = jsonObj.optString(HAPAttributeConstant.EXPRESSIONINFO_EXPRESSION);

		//read variable info part
		m_variables = new LinkedHashMap<String, HAPDataTypeCriteria>();
		JSONObject varInfosJson = jsonObj.optJSONObject(HAPAttributeConstant.EXPRESSIONINFO_VARIABLESINFO);
		Iterator<String> varNames = varInfosJson.keys();
		while(varNames.hasNext()){
			String varName = varNames.next();
			HAPDataTypeCriteria varInfo = HAPDataUtility.buildDataTypeCriteriaFromJson(varInfosJson);
			m_variables.put(varName, varInfo);
		}
		
		//process constants part
		m_constants = new LinkedHashMap<String, HAPData>();
		JSONObject constantsJson = jsonObj.optJSONObject(HAPAttributeConstant.EXPRESSIONINFO_CONSTANTS);
		Iterator<String> constantNames = constantsJson.keys();
		while(constantNames.hasNext()){
			String constantName = constantNames.next();
			HAPData constant = HAPDataUtility.buildDataWrapperJson(constantsJson); 
			m_constants.put(constantName, constant);
		}
	}
	
	@Override
	protected void buildObjectByJson(Object json){  this.buildObjectByJson(json);}

	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(HAPAttributeConstant.EXPRESSIONINFO_EXPRESSION, this.m_expression);
		jsonMap.put(HAPAttributeConstant.EXPRESSIONINFO_VARIABLESINFO, HAPJsonUtility.buildJson(this.m_variables, HAPSerializationFormat.JSON_FULL));
		jsonMap.put(HAPAttributeConstant.EXPRESSIONINFO_CONSTANTS, HAPJsonUtility.buildJson(this.m_constants, HAPSerializationFormat.JSON_FULL));
	}

	public String toString(){return HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON));}
}
