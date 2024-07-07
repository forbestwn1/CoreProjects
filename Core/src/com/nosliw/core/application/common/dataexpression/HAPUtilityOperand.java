package com.nosliw.core.application.common.dataexpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.valueport.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPInterfaceProcessOperand;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataTypeOperation;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.data.HAPOperationParmInfo;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.data.core.domain.entity.expression.data1.HAPExecutableEntityExpressionDataGroup;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPUtilityOperand {

	static public void replaceAttributeOpWithOperationOp(HAPWrapperOperand operand) {
		List<HAPWrapperOperand> attrOperands = new ArrayList<HAPWrapperOperand>();
		HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
			@Override
			public void postPross(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)){
					attrOperands.add(operand);
				}
			}

			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {		return true;	}
		});	

		for(int i=0; i<attrOperands.size(); i++) {
			HAPWrapperOperand attrOpWrapper = attrOperands.get(i);
			HAPOperandAttribute attrOp = (HAPOperandAttribute)attrOpWrapper.getOperand();
			
			List<HAPParmInOperationOperand> parms = new ArrayList<HAPParmInOperationOperand>();
			parms.add(new HAPParmInOperationOperand(HAPConstantShared.DATAOPERATION_COMPLEX_GETCHILDDATA_NAME, new HAPOperandConstant(new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), attrOp.getAttribute()))));
			HAPOperandOperation opOperand = new HAPOperandOperation(attrOp.getBase().getOperand(), HAPConstantShared.DATAOPERATION_COMPLEX_GETCHILDDATA, parms);
			attrOpWrapper.setOperand(opOperand);
		}
	}
	
	//process variables in expression, 
	//	for attribute operation a.b.c.d which have responding definition in context, 
	//			replace attribute operation with one variable operation
	//  for attribute operation a.b.c.d which have responding definition a.b.c in context, 
	//			replace attribute operation with one variable operation(a.b.c) and getChild operation
	public static void processAttributeOperandInExpressionOperand(HAPWrapperOperand orgOperand, final Set<String> dataVarNames, Set<String> dataConstantNames){
		List<HAPAttributeOperandChainInfo> data = new ArrayList<HAPAttributeOperandChainInfo>();
		HAPUtilityOperand.processAllOperand(orgOperand, data, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				List<HAPAttributeOperandChainInfo> stack = (List<HAPAttributeOperandChainInfo>)data;
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)){
					HAPOperandAttribute attrOperand = (HAPOperandAttribute)operand.getOperand();
					HAPAttributeOperandChainInfo attrInfo = null;
					if(stack.size()==0 || !stack.get(stack.size()-1).open){
						//new attrInfo
						attrInfo = new HAPAttributeOperandChainInfo();
						attrInfo.lastAttrOperand = operand;
						stack.add(attrInfo);
					}
					else{
						//use latest
						attrInfo = stack.get(stack.size()-1);
					}
					attrInfo.insertSegment(attrOperand.getAttribute());
				}
				else{
					if(stack.size()>=1 && stack.get(stack.size()-1).open){
						HAPAttributeOperandChainInfo attrInfo = stack.get(stack.size()-1);
						attrInfo.startOperand = operand;
						attrInfo.open = false;
					}
				}
				return true;
			}
			
			@Override
			public void postPross(HAPWrapperOperand operand, Object data){
				List<HAPAttributeOperandChainInfo> stack = (List<HAPAttributeOperandChainInfo>)data;
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)){
					HAPOperandAttribute attrOperand = (HAPOperandAttribute)operand.getOperand();
					HAPAttributeOperandChainInfo attrInfo = stack.get(stack.size()-1);
					attrInfo.veryPath.add(attrOperand.getAttribute());
					if(attrInfo.path.size()==attrInfo.veryPath.size()){
						//at end of attribute chain
						processAttributeChain(attrInfo, dataVarNames, dataConstantNames);
						stack.remove(stack.size()-1);
					}
				}
				else{
					if(stack.size()>=1 && !stack.get(stack.size()-1).open && stack.get(stack.size()-1).startOperand==operand){
						//find start operand
						stack.get(stack.size()-1).open = true;
					}
				}
			}
		});
	}
	
	private static void processAttributeChain(HAPAttributeOperandChainInfo attrInfo, Set<String> dataVarNames, Set<String> dataConstantNames){
		String startOpType = attrInfo.startOperand.getOperand().getType();
		if(startOpType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
			//attribute start with variable
			HAPOperandVariable varOperand = (HAPOperandVariable)attrInfo.startOperand.getOperand();
			attrInfo.path.add(0, varOperand.getVariableName());
			String path = HAPUtilityNamingConversion.cascadePath(attrInfo.path.toArray(new String[0]));
			
			Map<String, String> opTypeByName = new LinkedHashMap<String, String>();
			for(String varName : dataVarNames) {
				opTypeByName.put(varName, HAPConstantShared.EXPRESSION_OPERAND_VARIABLE);
			}
			for(String constantName : dataConstantNames) {
				opTypeByName.put(constantName, HAPConstantShared.EXPRESSION_OPERAND_CONSTANT);
			}
			
			for(String name : opTypeByName.keySet()){
				if(path.equals(name)){
					String opType = opTypeByName.get(name);
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)) {
						attrInfo.lastAttrOperand.setOperand(new HAPOperandVariable(name));  //replace with variable operand
					} else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT))
					 {
						attrInfo.lastAttrOperand.setOperand(new HAPOperandConstant(name));  //replace with constant operand
					}
					break;
				}
				else if(path.startsWith(name+".")){
					//replace with variable operand.child
					HAPOperand operand = new HAPOperandVariable(name);
					String[] pathSegs = HAPUtilityNamingConversion.parsePaths(path.substring(name.length()+1));
					for(String pathSeg : pathSegs){
						List<HAPParmInOperationOperand> parms = new ArrayList<HAPParmInOperationOperand>();
						parms.add(new HAPParmInOperationOperand(HAPConstantShared.DATAOPERATION_COMPLEX_GETCHILDDATA_NAME, new HAPOperandConstant("#string:simple:"+pathSeg)));
						operand = new HAPOperandOperation(operand, HAPConstantShared.DATAOPERATION_COMPLEX_GETCHILDDATA, parms);
					}
					attrInfo.lastAttrOperand.setOperand(operand);
					break;
				}
			}
		}
	}
	
	
//	static public void updateReferencedExecute(HAPOperandWrapper operand, Map<String, HAPExecutableTask> references) {
//		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
//			@Override
//			public boolean processOperand(HAPOperandWrapper operand, Object data) {
//				String opType = operand.getOperand().getType();
//				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
//					HAPDefinitionOperandReference referenceOperand = (HAPDefinitionOperandReference)operand.getOperand();
//					referenceOperand.updateReferenceExecute(references);
//				}
//				return true;
//			}
//		});	
//	}

	static public void updateNameInOperand(HAPWrapperOperand operand, HAPUpdateName nameUpdate, String[] operandTypes) {
		Set<String> types = new HashSet<String>(Arrays.asList(operandTypes));
		//update variable operand
		HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(types.contains(opType)) {
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableChild = (HAPOperandVariable)operand.getOperand();
						String newName = nameUpdate.getUpdatedName(variableChild.getVariableName()); 
						if(newName!=null) {
							variableChild.setVariableName(newName);
						}
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceChild = (HAPOperandReference)operand.getOperand();
						
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)){
						HAPOperandConstant constantChild = (HAPOperandConstant)operand.getOperand();
						if(HAPUtilityBasic.isStringNotEmpty(constantChild.getName())){
							String newName = nameUpdate.getUpdatedName(constantChild.getName()); 
							constantChild.setName(newName);
						}
					}
				}
				return true;
			}
		});	
	}

	static public Set<String> discoverReferences(HAPWrapperOperand operand) {
		Set<String> out = new HashSet<String>();
		HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					out.add(referenceOperand.getReference());
				}
				return true;
			}
		});	
		return out;
	}
	
	
	static public Set<String> discoverVariableNames(HAPWrapperOperand operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				Set<String> varNames = (Set<String>)data;
				switch(operand.getOperand().getType()){
				case HAPConstantShared.EXPRESSION_OPERAND_VARIABLE:
					HAPOperandVariable varOperand = (HAPOperandVariable)operand.getOperand();
					varNames.add(varOperand.getVariableName());
					break;
				}
				return true;
			}
		});
		return out;
	}

	static public Set<String> discoverVariableKeys(HAPWrapperOperand operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				Set<String> varIds = (Set<String>)data;
				switch(operand.getOperand().getType()){
				case HAPConstantShared.EXPRESSION_OPERAND_VARIABLE:
					HAPOperandVariable varOperand = (HAPOperandVariable)operand.getOperand();
					varIds.add(varOperand.getVariableKey());
					break;
				}
				return true;
			}
		});
		return out;
	}

	static public Set<HAPWrapperOperand> discoverVariableOperands(HAPWrapperOperand operand){
		Set<HAPWrapperOperand> out = new HashSet<HAPWrapperOperand>();
		processAllOperand(operand, out, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				Set<HAPWrapperOperand> varOperands = (Set<HAPWrapperOperand>)data;
				switch(operand.getOperand().getType()){
				case HAPConstantShared.EXPRESSION_OPERAND_VARIABLE:
					varOperands.add(operand);
					break;
				}
				return true;
			}
		});
		return out;
	}

	/**
	 * Find all the unsolved constants names: constants that only provide name
	 * @param operand
	 * @return
	 */
	static public Set<String> discoveryUnsolvedConstants(HAPWrapperOperand operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				Set<String> vars = (Set<String>)data;
				switch(operand.getOperand().getType()){
				case HAPConstantShared.EXPRESSION_OPERAND_CONSTANT:
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
					if(constantOperand.getData()==null){
						vars.add(constantOperand.getName());
					}
					break;
				}
				return true;
			}
		});
		return out;
	}
	
	static public void processAllOperand(HAPWrapperOperand operand, Object data, HAPInterfaceProcessOperand task){
		if(task.processOperand(operand, data)){
			List<HAPWrapperOperand> children = operand.getOperand().getChildren();
			for(HAPWrapperOperand child : children){
				HAPUtilityOperand.processAllOperand(child, data, task);
			}
			task.postPross(operand, data);
		}
	}
	
	static public void updateConstantData(HAPWrapperOperand operand, final Map<String, HAPData> contextConstants){
		if(contextConstants!=null && !contextConstants.isEmpty()) {
			HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)){
						HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
						if(constantOperand.getData()==null){
							String constantName = constantOperand.getName();
							HAPData constantData = contextConstants.get(constantName);
							constantOperand.setData(constantData);
						}
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable varOperand = (HAPOperandVariable)operand.getOperand();
						HAPData cstData = contextConstants.get(varOperand.getVariableName());
						if(cstData!=null) {
							//find constant value for variable, replace with constant
							HAPOperandConstant constantOperand = new HAPOperandConstant(cstData);
							operand.setOperand(constantOperand);
						}
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						HAPExecutableEntityExpressionDataGroup refExpGroup = (HAPExecutableEntityExpressionDataGroup)referenceOperand.getReferedExpression();
						Map<String, HAPExecutableExpressionData1> expItems = refExpGroup.getAllExpressionItems();
						for(HAPExecutableExpressionData1 expItem : expItems.values()) {
							updateConstantData(expItem.getOperand(), refExpGroup.getDataConstants());
						}
					}
					return true;
				}
			});	
		}
	}

	static public HAPContainerVariableInfo discover(
			List<HAPOperand> operands, 
			List<HAPDataTypeCriteria> expectOutputs,
			HAPContainerVariableInfo inVariablesInfo, 
			List<HAPMatchers> matchers,
			HAPDataTypeHelper dataTypeHelper,
			HAPProcessTracker processTracker) {
		//do discovery on operand
		HAPContainerVariableInfo varsInfo = inVariablesInfo.clone();
		
		HAPContainerVariableInfo oldVarsInfo;
		//Do discovery until local vars definition not change or fail 
		do{
			matchers.clear();
			oldVarsInfo = varsInfo;
			varsInfo = varsInfo.clone();
			processTracker.clear();
			for(int i=0; i<operands.size(); i++) {
				HAPDataTypeCriteria expectOutput = null;
				if(expectOutputs==null||expectOutputs.get(i)==null) {
					expectOutput = HAPDataTypeCriteriaAny.getCriteria(); 
				}
				
				matchers.add(operands.get(i).discover(varsInfo, expectOutput, processTracker, dataTypeHelper));
			}
		}while(!HAPUtilityBasic.isEqualMaps(varsInfo.getVariableCriteriaInfos(), oldVarsInfo.getVariableCriteriaInfos()) && processTracker.isSuccess());
		return varsInfo;
	}

	/**
	 * Process anonomouse parameter in operaion
	 * Add parm name to it
	 * It only works for OperationOperand with clear data typeId
	 * @param expression
	 */
	public void processDefaultAnonomousParmInOperation(HAPWrapperOperand operand, HAPDataTypeHelper dataTypeHelper){
		HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_OPERATION)){
					HAPOperandOperation operationOperand = (HAPOperandOperation)operand.getOperand();
					HAPDataTypeId dataTypeId = operationOperand.getDataTypeId();
					if(dataTypeId!=null){
						HAPDataTypeOperation dataTypeOperation = dataTypeHelper.getOperationInfoByName(dataTypeId, operationOperand.getOperaion());
						List<HAPOperationParmInfo> parmsInfo = dataTypeOperation.getOperationInfo().getParmsInfo();
						Map<String, HAPWrapperOperand> parms = operationOperand.getParms();
						for(HAPOperationParmInfo parmInfo : parmsInfo){
							HAPWrapperOperand parmOperand = parms.get(parmInfo.getName());
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

}

class HAPAttributeOperandChainInfo{
	public HAPWrapperOperand startOperand;
	
	public HAPWrapperOperand lastAttrOperand;
	
	public List<String> path;
	
	public List<String> veryPath;
	
	public boolean open;
	
	public HAPAttributeOperandChainInfo(){
		this.path = new ArrayList<String>();
		this.veryPath = new ArrayList<String>();
		this.open = true;
	}
	
	public void insertSegment(String seg){
		path.add(0, seg);
	}
}

