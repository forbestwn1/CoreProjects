package com.nosliw.expression;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.expression.utils.HAPAttributeConstant;

/*
 * store information related with expression
 * 		expression string
 * 		constantDatas : the constant data to put into expression
 * 		variableInfos : data type information for variables in expression 
 */
public class HAPExpressionInfo implements HAPStringable{

	//expression string
	private String m_expression;
	
	//constants value info
	private Map<String, HAPData> m_constantDatas;
	
	//variable data type info
	private Map<String, HAPDataTypeInfo> m_variableInfos;
	
	public HAPExpressionInfo(String expression, Map<String, HAPData> constantDatas, Map<String, HAPDataTypeInfo> variableInfos){
		this.m_expression = expression;
		
		if(constantDatas==null)   this.m_constantDatas = new LinkedHashMap<String, HAPData>();
		else   this.m_constantDatas = constantDatas;
		
		if(variableInfos==null)   this.m_variableInfos = new LinkedHashMap<String, HAPDataTypeInfo>();
		else  	this.m_variableInfos = variableInfos;
	}

	public HAPExpressionInfo addVariableInfo(Map<String, HAPDataTypeInfo> m_variableInfos){
		this.m_variableInfos.putAll(m_variableInfos);
		return this;
	}
	
	public String getExpression(){ return this.m_expression; }
	public Map<String, HAPData> getConstantDatas(){return this.m_constantDatas;}
	public Map<String, HAPDataTypeInfo> getVariableInfos(){ return this.m_variableInfos; }

	
	public static HAPExpressionInfo parse(JSONObject jsonObj, HAPDataTypeManager dataTypeMan){
		String expression = jsonObj.optString(HAPAttributeConstant.ATTR_EXPRESSIONINFO_EXPRESSION);

		//read variable infos
		Map<String, HAPDataTypeInfo> varDataTypeInfos = new LinkedHashMap<String, HAPDataTypeInfo>();
		JSONObject varInfosJson = jsonObj.optJSONObject(HAPAttributeConstant.ATTR_EXPRESSIONINFO_VARIABLESINFO);
		Iterator<String> varNames = varInfosJson.keys();
		while(varNames.hasNext()){
			String varName = varNames.next();
			HAPDataTypeInfo varInfo = HAPDataTypeInfo.parse(varInfosJson.optJSONObject(varName));
			varDataTypeInfos.put(varName, varInfo);
		}
		
		Map<String, HAPData> constantInfos = new LinkedHashMap<String, HAPData>();
		JSONObject constantsJson = jsonObj.optJSONObject(HAPAttributeConstant.ATTR_EXPRESSIONINFO_CONSTANTS);
		Iterator<String> constantNames = constantsJson.keys();
		while(constantNames.hasNext()){
			String constantName = constantNames.next();
			HAPData constant = dataTypeMan.parseJson(constantsJson.optJSONObject(constantName), null, null); 
			constantInfos.put(constantName, constant);
		}
		
		return new HAPExpressionInfo(expression, constantInfos, varDataTypeInfos);
	}
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(HAPAttributeConstant.ATTR_EXPRESSIONINFO_EXPRESSION, this.m_expression);
		jsonMap.put(HAPAttributeConstant.ATTR_EXPRESSIONINFO_VARIABLESINFO, HAPJsonUtility.getMapObjectJson(this.m_variableInfos));
		jsonMap.put(HAPAttributeConstant.ATTR_EXPRESSIONINFO_CONSTANTS, HAPJsonUtility.getMapObjectJson(this.m_constantDatas));
		return HAPJsonUtility.getMapJson(jsonMap);
	}
	
	public String toString(){return HAPJsonUtility.formatJson(this.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));}
}
