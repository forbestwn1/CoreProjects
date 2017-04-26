package com.nosliw.data.core.imp.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPOperand;

/**
 * Parsed expression 
 */
public class HAPExpressionImp extends HAPSerializableImp implements HAPExpression{

	// original expressiong
	private HAPExpressionInfo m_expressionInfo;

	// parsed expression
	private HAPOperand m_operand;
	
	//mutiple messages
	private List<String> m_errorMsgs;
	
	// store all variable information in expression (variable name -- variable data type infor)
	// for variable that we don't know data type, its value in this map is null
	private Map<String, HAPDataTypeCriteria> m_varsInfo;
	
	//normalized variable information -- variable criteria with root from data type
	private Map<String, HAPDataTypeCriteria> m_normalizedVarsInfo;
	
	public HAPExpressionImp(HAPExpressionInfo expressionInfo, HAPOperand operand){
		this.m_errorMsgs = new ArrayList<String>();
		this.m_expressionInfo = expressionInfo;
		this.m_operand = operand;
		this.m_varsInfo = new LinkedHashMap<String, HAPDataTypeCriteria>();
		this.m_varsInfo.putAll(this.m_expressionInfo.getVariables());
	}
	
	@Override
	public HAPExpressionInfo getExpressionInfo() {		return this.m_expressionInfo;	}

	@Override
	public HAPOperand getOperand() {  return this.m_operand;  }

	@Override
	public Map<String, HAPDataTypeCriteria> getVariables() {
		if(this.m_normalizedVarsInfo!=null)  return this.m_normalizedVarsInfo;
		else return this.m_varsInfo;
	}
	
	public void setVariables(Map<String, HAPDataTypeCriteria> vars){
		this.m_varsInfo.clear();
		this.m_varsInfo.putAll(vars);
	}
	
	@Override
	public String[] getErrorMessages() {
		if(this.m_errorMsgs==null || this.m_errorMsgs.size()==0)  return null;
		return this.m_errorMsgs.toArray(new String[0]);	
	}
	public void addErrorMessage(String msg){  this.m_errorMsgs.add(msg);  } 
	public void addErrorMessages(List<String> msgs){  this.m_errorMsgs.addAll(msgs);  } 
	
	/**
	 * Build normalized variable info, then normalize operand according to normalized variables
	 * It means reducing all the redundant data type for each variable
	 * When a criteria has some data type that has parent belong to the same criteria, then we consider that data type as redundant
	 * As we we only need to keep the parent data type only 
	 */
	public void buildNormalizedVariablesInfo(HAPDataTypeHelper dataTypeHelper){
		this.m_normalizedVarsInfo = new LinkedHashMap<String, HAPDataTypeCriteria>();
		for(String varName : this.m_varsInfo.keySet()){
			HAPDataTypeCriteria criteria = this.m_varsInfo.get(varName);
			if(criteria!=null){
				this.m_normalizedVarsInfo.put(varName, criteria.normalize(dataTypeHelper));
			}
			else{
				this.m_normalizedVarsInfo.put(varName, null);
			}
		}
		this.getOperand().normalize(m_normalizedVarsInfo, dataTypeHelper);
	}

	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(EXPRESSIONINFO, HAPSerializeManager.getInstance().toStringValue(this.m_expressionInfo, HAPSerializationFormat.JSON));
		jsonMap.put(OPERAND, HAPSerializeManager.getInstance().toStringValue(this.m_operand, HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLES, HAPJsonUtility.buildJson(this.getVariables(), HAPSerializationFormat.JSON));
		jsonMap.put(ERRORMSGS, HAPJsonUtility.buildJson(m_errorMsgs, HAPSerializationFormat.JSON));
	}
}
