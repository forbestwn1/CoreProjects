package com.nosliw.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperand;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.data.utils.HAPDataUtility;
import com.nosliw.expression.utils.HAPAttributeConstant;
import com.nosliw.expression.utils.HAPExpressionUtility;
import com.nosliw.expression.utils.HAPIterateOperandTask;
import com.nosliw.expression.utils.HAPIterateOperandTaskOut;

/*
 * expression class
 * expression can be preprocessed
 *  
 */
public class HAPExpression implements HAPStringable{
	// original expressiong
	private String m_expression;
	// parsed expression
	private HAPOperand m_operand;
	// store all variable information in expression (variable name -- variable data type infor)
	// for variable that we don't know data type, its value in this map is null
	private Map<String, HAPDataTypeInfo> m_varsInfo;
	//whether this expression is script runnable
	private boolean m_isScriptRunnable = false;
	
	//store all data type info related with this expression
	//this information is mainly for javascript side as it has to load data type on demand
	private Set<HAPDataTypeInfo> m_allDataTypeInfo;
	
	//store constant data to used in expression
	private Map<String, HAPData> m_constantDatas; 
	
	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPExpression(HAPExpressionInfo expressionInfo, HAPDataTypeManager dataTypeMan){
		this.m_expression = expressionInfo.getExpression();
		this.m_varsInfo = expressionInfo.getVariableInfos();
		this.m_allDataTypeInfo = new HashSet<HAPDataTypeInfo>();
		
		this.m_constantDatas = expressionInfo.getConstantDatas();
		//add variable infor related with constant data
		for(String constantName : this.m_constantDatas.keySet()){
			this.m_varsInfo.put(constantName, HAPDataUtility.getDataTypeInfo(this.m_constantDatas.get(constantName)));
		}
		
		this.m_dataTypeMan = dataTypeMan;
		
		//collect variable and data type info 
		this.preProcessExpression();

		//add variable info to data type infos
		for(String varName : this.m_varsInfo.keySet()){
			HAPDataTypeInfo dataTypeInfo = this.m_varsInfo.get(varName);
			if(dataTypeInfo!=null)			this.m_allDataTypeInfo.add(dataTypeInfo);
		}

		//build path operand from attribute operand
		this.buildPathOperand();
		
		this.m_isScriptRunnable = this.m_operand.isScriptRunnable("");
	}

	/*
	 * process all attribute operand to create path operand
	 */
	private void buildPathOperand(){
		HAPIterateOperandTaskOut itOut = HAPExpressionUtility.iterateOperand(m_operand, new HAPIterateOperandTask(){
			@Override
			public HAPIterateOperandTaskOut process(HAPOperand operand, Object data, boolean isRoot) {
				HAPIterateOperandTaskOut taskOut = new HAPIterateOperandTaskOut();
				
				switch(operand.getOperandType()){
				case HAPConstant.EXPRESSION_OPERAND_CONSTANT:
				case HAPConstant.EXPRESSION_OPERAND_VARIABLE:
				case HAPConstant.EXPRESSION_OPERAND_DATAOPERATION:
				case HAPConstant.EXPRESSION_OPERAND_DATATYPEOPERATION:
				case HAPConstant.EXPRESSION_OPERAND_PATHOPERATION:
				{
					break;
				}
				case HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION:
				{
					HAPOperandAttribute attributeOperand = (HAPOperandAttribute)operand;
					taskOut.outOperand = new HAPOperandPath(attributeOperand, m_dataTypeMan);
					taskOut.toChild = false;
					break;
				}
				}
				return taskOut;
			}
		}, null);
		//replace operand in expression
		if(itOut.outOperand!=null)   this.m_operand = itOut.outOperand;
	}
	
	public Set<String> getAllVariableNames(){
		return this.m_varsInfo.keySet();
	}
	
	/*
	 * return all the variable and path variables, for instance a.b.c
	 */
	public Set<String> getAllPathVariables(){
		Set<String> out = new HashSet<String>();
		
		HAPIterateOperandTaskOut itOut = HAPExpressionUtility.iterateOperand(m_operand, new HAPIterateOperandTask(){
			@Override
			public HAPIterateOperandTaskOut process(HAPOperand operand, Object data, boolean isRoot) {
				HAPIterateOperandTaskOut taskOut = new HAPIterateOperandTaskOut();
				taskOut.childData = data;
				
				Set<String> paths = (Set<String>)data;
				
				switch(operand.getOperandType()){
				case HAPConstant.EXPRESSION_OPERAND_CONSTANT:
				case HAPConstant.EXPRESSION_OPERAND_DATAOPERATION:
				case HAPConstant.EXPRESSION_OPERAND_DATATYPEOPERATION:
				case HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION:
				{
					break;
				}
				case HAPConstant.EXPRESSION_OPERAND_VARIABLE:
				{
					HAPOperandVariable varOperand = (HAPOperandVariable)operand;
					paths.add(varOperand.getVarName());
					break;
				}
				case HAPConstant.EXPRESSION_OPERAND_PATHOPERATION:
				{
					HAPOperandPath pathOperand = (HAPOperandPath)operand;
					HAPOperand baseOperand = pathOperand.getBaseData();
					if(baseOperand.getOperandType()==HAPConstant.EXPRESSION_OPERAND_VARIABLE){
						HAPOperandVariable varOperand = (HAPOperandVariable)baseOperand;
						paths.add(HAPNamingConversionUtility.cascadePath(varOperand.getVarName(), pathOperand.getPath()));
						taskOut.toChild = false;
					}
					break;
				}
				}
				return taskOut;
			}
		}, out);
		
		return out;
	}
	
	/*
	 * whether this expression is runnable under particular script
	 */
	public boolean isScriptRunnable(String script){		return this.m_isScriptRunnable;	}
	
	/*
	 * preprocess Expression, it includes:
	 * 		collect all the variable and its data type
	 *    	figure out all the operand's data type
	 */
	private void preProcessExpression(){
		this.m_operand = HAPExpressionParser.parseExpression(this.m_expression, this.m_dataTypeMan);
		//collect variable information
		this.m_operand.preProcess(m_varsInfo, m_allDataTypeInfo);
	}

	/*
	 * run the Expression
	 */
	public HAPData execute(Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars){
		//merge parms input with constantData, data in parms will override constantData
		Map<String, HAPData> dataParm = new LinkedHashMap<String, HAPData>();
		dataParm.putAll(this.m_constantDatas);
		if(parms!=null)  dataParm.putAll(parms);
		
		HAPData out = this.m_operand.execute(dataParm, wraperVars);
		return out;
	}

	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		jsonMap.put(HAPAttributeConstant.EXPRESSION_EXPRESSION, this.m_expression);
		jsonMap.put(HAPAttributeConstant.EXPRESSION_OPERAND, this.m_operand.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.EXPRESSION_SCRIPTRUNNALBE, this.isScriptRunnable(HAPConstant.OPERATIONDEF_SCRIPT_JAVASCRIPT)+"");

		jsonMap.put(HAPAttributeConstant.EXPRESSION_VARIABLESINFO, HAPJsonUtility.getMapObjectJson(this.m_varsInfo));
		
		
		Map<String, String> constantsJsons = new LinkedHashMap<String, String>();
		for(String name : m_constantDatas.keySet()){
			constantsJsons.put(name, m_constantDatas.get(name).toStringValue(HAPConstant.SERIALIZATION_JSON_FULL));
		}
		jsonMap.put(HAPAttributeConstant.EXPRESSION_CONSTANTS, HAPJsonUtility.getMapJson(constantsJsons));

		jsonMap.put(HAPAttributeConstant.EXPRESSION_ALLDATATYPEINFOS, HAPJsonUtility.getSetObjectJson(this.m_allDataTypeInfo));
		
		Map<String, Class> jsonTypeMap = new LinkedHashMap<String, Class>();
		jsonTypeMap.put(HAPAttributeConstant.EXPRESSION_SCRIPTRUNNALBE, Boolean.class);
		
		return HAPJsonUtility.getMapJson(jsonMap, jsonTypeMap);
	}	
	
	@Override
	public String toString(){return this.toStringValue(HAPConstant.SERIALIZATION_JSON);}
}
