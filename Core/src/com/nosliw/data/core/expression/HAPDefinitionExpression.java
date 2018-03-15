package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.operand.HAPOperandOperation;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;

public class HAPDefinitionExpression  extends HAPSerializableImp{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	@HAPAttribute
	public static String OPERAND = "operand";

	@HAPAttribute
	public static String VARIABLENAMES = "variableNames";

	@HAPAttribute
	public static String REFERENCENAMES = "referenceNames";

	
	private String m_expression;
	
	private HAPOperandWrapper m_operand;

	//all variables defined in expression
	private Set<String> m_variableNames;

	//all references defined in expression
	private Set<String> m_referenceNames;
	
	
	public HAPDefinitionExpression(String expression) {
		this.m_expression = expression;
		this.m_variableNames = new HashSet<String>();
		this.m_referenceNames = new HashSet<String>();
		this.process();
	}
	
	public String getExpression() {  return this.m_expression;  }
	
	public HAPOperandWrapper getOperand() {  return this.m_operand;    }
	
	public Set<String> getVariableNames() {		return this.m_variableNames;	}

	public Set<String> getReferenceNames() {		return this.m_referenceNames;	}

	
	
	private void process() {
		//parse expression
		this.m_operand = new HAPOperandWrapper(HAPExpressionManager.expressionParser.parseExpression(this.m_expression));

		this.m_variableNames.addAll(HAPExpressionUtility.discoverVariables(this.m_operand));
		
		this.discoverReferences();
		
		this.processDefaultAnonomousParmInOperation();
		
	}
	
	//find all variables and references in expression
	private void discoverReferences() {
		HAPOperandUtility.processAllOperand(this.m_operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					m_referenceNames.add(referenceOperand.getReferenceName());
				}
				return true;
			}
		});		
	}
	
	/**
	 * Process anonomouse parameter in operaion
	 * Add parm name to it
	 * It only works for OperationOperand with clear data typeId
	 * @param expression
	 */
	private void processDefaultAnonomousParmInOperation(){
		HAPOperandUtility.processAllOperand(this.m_operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_OPERATION)){
					HAPOperandOperation operationOperand = (HAPOperandOperation)operand.getOperand();
					HAPDataTypeId dataTypeId = operationOperand.getDataTypeId();
					if(dataTypeId!=null){
						HAPDataTypeOperation dataTypeOperation = HAPExpressionManager.dataTypeHelper.getOperationInfoByName(dataTypeId, operationOperand.getOperaion());
						List<HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
						Map<String, HAPOperandWrapper> parms = operationOperand.getParms();
						for(HAPOperationParmInfo parmInfo : parmsInfo){
							HAPOperandWrapper parmOperand = parms.get(parmInfo.getName());
							if(parmOperand==null && parmInfo.getIsBase() && operationOperand.getBase()!=null){
								//if parmInfo is base parm and is located in base
								parmOperand = operationOperand.getBase();
								operationOperand.addParm(parmInfo.getName(), parmOperand.getOperand());
								operationOperand.setBase(null);
							}
						}
					}
				}
				return true;
			}
		});		
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, this.m_expression);
		jsonMap.put(VARIABLENAMES, HAPJsonUtility.buildJson(this.m_variableNames, HAPSerializationFormat.JSON));
		jsonMap.put(REFERENCENAMES, HAPJsonUtility.buildJson(this.m_referenceNames, HAPSerializationFormat.JSON));
		jsonMap.put(OPERAND, HAPJsonUtility.buildJson(this.m_operand, HAPSerializationFormat.JSON));
	}

}
