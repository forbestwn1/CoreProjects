package com.nosliw.data.core.operand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataTypeOperation;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.data.HAPOperationParmInfo;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableExpression;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPOperandUtility {

	static public void replaceAttributeOpWithOperationOp(HAPOperandWrapper operand) {
		List<HAPOperandWrapper> attrOperands = new ArrayList<HAPOperandWrapper>();
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public void postPross(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)){
					attrOperands.add(operand);
				}
			}

			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {		return true;	}
		});	

		for(int i=0; i<attrOperands.size(); i++) {
			HAPOperandWrapper attrOpWrapper = attrOperands.get(i);
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
	public static void processAttributeOperandInExpressionOperand(HAPOperandWrapper orgOperand, final Set<String> dataVarNames, Set<String> dataConstantNames){
		List<HAPAttributeOperandChainInfo> data = new ArrayList<HAPAttributeOperandChainInfo>();
		HAPOperandUtility.processAllOperand(orgOperand, data, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
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
			public void postPross(HAPOperandWrapper operand, Object data){
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
			for(String varName : dataVarNames)   opTypeByName.put(varName, HAPConstantShared.EXPRESSION_OPERAND_VARIABLE);
			for(String constantName : dataConstantNames)   opTypeByName.put(constantName, HAPConstantShared.EXPRESSION_OPERAND_CONSTANT);
			
			for(String name : opTypeByName.keySet()){
				if(path.equals(name)){
					String opType = opTypeByName.get(name);
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE))   attrInfo.lastAttrOperand.setOperand(new HAPOperandVariable(name));  //replace with variable operand
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT))   attrInfo.lastAttrOperand.setOperand(new HAPOperandConstant(name));  //replace with constant operand
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
//					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
//					referenceOperand.updateReferenceExecute(references);
//				}
//				return true;
//			}
//		});	
//	}

	static public void updateNameInOperand(HAPOperandWrapper operand, HAPUpdateName nameUpdate, String[] operandTypes) {
		Set<String> types = new HashSet<String>(Arrays.asList(operandTypes));
		//update variable operand
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(types.contains(opType)) {
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableChild = (HAPOperandVariable)operand.getOperand();
						String newName = nameUpdate.getUpdatedName(variableChild.getVariableName()); 
						if(newName!=null)	variableChild.setVariableName(newName);
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

	static public Set<String> discoverReferences(HAPOperandWrapper operand) {
		Set<String> out = new HashSet<String>();
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
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
	
	
	static public Set<String> discoverVariableNames(HAPOperandWrapper operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
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

	static public Set<String> discoverVariableIds(HAPOperandWrapper operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				Set<String> varIds = (Set<String>)data;
				switch(operand.getOperand().getType()){
				case HAPConstantShared.EXPRESSION_OPERAND_VARIABLE:
					HAPOperandVariable varOperand = (HAPOperandVariable)operand.getOperand();
					varIds.add(varOperand.getVariableId());
					break;
				}
				return true;
			}
		});
		return out;
	}

	static public Set<HAPOperandWrapper> discoverVariableOperands(HAPOperandWrapper operand){
		Set<HAPOperandWrapper> out = new HashSet<HAPOperandWrapper>();
		processAllOperand(operand, out, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				Set<HAPOperandWrapper> varOperands = (Set<HAPOperandWrapper>)data;
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
	static public Set<String> discoveryUnsolvedConstants(HAPOperandWrapper operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
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
	
	static public void processAllOperand(HAPOperandWrapper operand, Object data, HAPOperandTask task){
		if(task.processOperand(operand, data)){
			List<HAPOperandWrapper> children = operand.getOperand().getChildren();
			for(HAPOperandWrapper child : children){
				HAPOperandUtility.processAllOperand(child, data, task);
			}
			task.postPross(operand, data);
		}
	}
	
	static public void updateConstantData(HAPOperandWrapper operand, final Map<String, HAPData> contextConstants){
		if(contextConstants!=null && !contextConstants.isEmpty()) {
			HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
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
						HAPExecutableExpressionGroup refExpGroup = (HAPExecutableExpressionGroup)referenceOperand.getReferedExpression();
						Map<String, HAPExecutableExpression> expItems = refExpGroup.getExpressionItems();
						for(HAPExecutableExpression expItem : expItems.values()) {
							updateConstantData(expItem.getOperand(), refExpGroup.getDataConstants());
						}
					}
					return true;
				}
			});	
		}
	}

	static public HAPContainerVariableCriteriaInfo discover(
			List<HAPOperand> operands, 
			List<HAPDataTypeCriteria> expectOutputs,
			HAPContainerVariableCriteriaInfo inVariablesInfo, 
			Map<String, HAPInfoCriteria> outVariablesInfo,
			List<HAPMatchers> matchers,
			HAPDataTypeHelper dataTypeHelper,
			HAPProcessTracker processTracker) {
		//do discovery on operand
		HAPContainerVariableCriteriaInfo varsInfo = inVariablesInfo.clone();
		
		HAPContainerVariableCriteriaInfo oldVarsInfo;
		//Do discovery until local vars definition not change or fail 
		do{
			matchers.clear();
			oldVarsInfo = varsInfo;
			varsInfo = varsInfo.clone();
			processTracker.clear();
			for(int i=0; i<operands.size(); i++) {
				matchers.add(operands.get(i).discover(varsInfo, expectOutputs.get(i), processTracker, dataTypeHelper));
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
	public void processDefaultAnonomousParmInOperation(HAPOperandWrapper operand, HAPDataTypeHelper dataTypeHelper){
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_OPERATION)){
					HAPOperandOperation operationOperand = (HAPOperandOperation)operand.getOperand();
					HAPDataTypeId dataTypeId = operationOperand.getDataTypeId();
					if(dataTypeId!=null){
						HAPDataTypeOperation dataTypeOperation = dataTypeHelper.getOperationInfoByName(dataTypeId, operationOperand.getOperaion());
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

}

class HAPAttributeOperandChainInfo{
	public HAPOperandWrapper startOperand;
	
	public HAPOperandWrapper lastAttrOperand;
	
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

