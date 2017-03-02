package com.nosliw.data.expression;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeCriteria;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.expression.utils.HAPAttributeConstant;

/*
 * ExpressionInfo is the expression definition unit  
 * store information related with expression, it include expression itself and its configuration:
 * 		expression itself
 * 		name : name of expression. so that other expression can reference it by name
 * 		constants : the constant data to put into expression
 * 		variables : data type information for variables in expression
 * 		references : reference information in expression 
 */
public class HAPExpressionInfo extends HAPStringableValueEntity{

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String CONSTANTS = "constants";
	
	@HAPAttribute
	public static String VARIABLES = "variables";
	
	@HAPAttribute
	public static String REFERENCES = "references";

	//the name of the expression
	private String m_name;
	
	//expression string
	private String m_expression;
	
	//constants value info
	private Map<String, HAPData> m_constants;
	
	//variable data type info
	private Map<String, HAPDataTypeCriteria> m_variables;
	
	private Map<String, HAPReferenceInfo> m_references;
	
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
	protected boolean buildObjectByFullJson(Object json){
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
		return true;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		this.buildObjectByJson(json);
		return true;
	}

	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(HAPAttributeConstant.EXPRESSIONINFO_EXPRESSION, this.m_expression);
		jsonMap.put(HAPAttributeConstant.EXPRESSIONINFO_VARIABLESINFO, HAPJsonUtility.buildJson(this.m_variables, HAPSerializationFormat.JSON_FULL));
		jsonMap.put(HAPAttributeConstant.EXPRESSIONINFO_CONSTANTS, HAPJsonUtility.buildJson(this.m_constants, HAPSerializationFormat.JSON_FULL));
	}

	public String toString(){return HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON));}
}
