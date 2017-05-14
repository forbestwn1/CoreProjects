package com.nosliw.data.core.imp.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPConverters;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPProcessVariablesContext;
import com.nosliw.data.core.expression.HAPVariableInfo;

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
	private Map<String, HAPVariableInfo> m_varsInfo;
	
	//store all the converter for every variables in this expression
	//it convert variable from caller to variable in expression
	private Map<String, HAPConverters> m_converters;
	
	public HAPExpressionImp(HAPExpressionInfo expressionInfo, HAPOperand operand){
		this.m_errorMsgs = new ArrayList<String>();
		this.m_expressionInfo = expressionInfo;
		this.m_operand = operand;
		
		//build vars info
		this.m_varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Map<String, HAPDataTypeCriteria> varCriterias = this.m_expressionInfo.getVariableCriterias();
		for(String varName : varCriterias.keySet()){
			this.m_varsInfo.put(varName, new HAPVariableInfo(varCriterias.get(varName)));
		}
	}
	
	@Override
	public HAPExpressionInfo getExpressionInfo() {		return this.m_expressionInfo;	}

	@Override
	public HAPOperand getOperand() {  return this.m_operand;  }

	@Override
	public Map<String, HAPVariableInfo> getVariables() {		return this.m_varsInfo;	}
	
	@Override
	public Map<String, HAPConverters> getVariableConverters(){		return this.m_converters;	}
	
	@Override
	public HAPConverters discover(Map<String, HAPVariableInfo> expectVariablesInfo, HAPDataTypeCriteria expectCriteria, HAPProcessVariablesContext context,	HAPDataTypeHelper dataTypeHelper){

		//update variables in expression
		for(String varName : this.getVariables().keySet()){
			HAPVariableInfo expressionVar = this.getVariables().get(varName);
			HAPVariableInfo expectVar = expectVariablesInfo.get(varName);
			if(expectVar==null){
			}
			if(expressionVar.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_CLOSE)){
			}
			else{
				HAPDataTypeCriteria adjustedCriteria = dataTypeHelper.merge(expressionVar.getCriteria(), expectVar.getCriteria());
				expressionVar.setCriteria(adjustedCriteria);
			}
		}
		
		//do discovery
		Map<String, HAPVariableInfo> expressionVars = new LinkedHashMap<String, HAPVariableInfo>();
		expressionVars.putAll(this.getVariables());
		
		HAPConverters converter = null;
		Map<String, HAPVariableInfo> oldVars;
		//Do discovery until vars not change or fail 
		do{
			oldVars = new LinkedHashMap<String, HAPVariableInfo>();
			oldVars.putAll(expressionVars);
			
			context.clear();
			converter = this.getOperand().discover(expressionVars, expectCriteria, context, dataTypeHelper);
		}while(!HAPBasicUtility.isEqualMaps(expressionVars, oldVars) && context.isSuccess());
		
		//merge again, cal variable converters, update expect variable
		for(String varName : this.getVariables().keySet()){
			HAPVariableInfo expressionVar = this.getVariables().get(varName);
			HAPVariableInfo expectVar = expectVariablesInfo.get(varName);
			if(expectVar==null){
				expectVar = new HAPVariableInfo();
				expectVar.setCriteria(expressionVar.getCriteria());
				expectVariablesInfo.put(varName, expectVar);
			}
			if(expectVar.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_CLOSE)){
			}
			else{
				expectVar.setCriteria(expressionVar.getCriteria());
			}

			//cal var converters
			HAPConverters varConverters = dataTypeHelper.buildConvertor(expectVar.getCriteria(), expressionVar.getCriteria());
			this.m_converters.put(varName, varConverters);
		}
		
		return converter;
	}
	
	
	public void setVariables(Map<String, HAPVariableInfo> vars){
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
