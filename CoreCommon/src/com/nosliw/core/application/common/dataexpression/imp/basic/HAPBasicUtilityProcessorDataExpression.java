package com.nosliw.core.application.common.dataexpression.imp.basic;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPResourceDataBrick;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperand;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandAttribute;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandOperation;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandReference;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandVariable;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.valueport.HAPUtilityResovleElement;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPUtilityResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.xxx.application.valueport.HAPInfoElementResolve;

public class HAPBasicUtilityProcessorDataExpression {
	
	public static HAPBasicExpressionData buildBasicDataExpression(HAPDefinitionDataExpression dataExpressionDef) {
		return new HAPBasicExpressionData(HAPBasicUtilityProcessorDataExpression.buildBasicOperand(dataExpressionDef.getOperand()));
	}
	
	public static HAPBasicOperand buildBasicOperand(HAPDefinitionOperand operandDef) {
		HAPBasicOperand out = null;
		
		String operandType = operandDef.getType();
		
		if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)) {
			out = buildBasicOperandAttribute((HAPDefinitionOperandAttribute)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)) {
			out = buildBasicOperandConstant((HAPDefinitionOperandConstant)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)) {
			out = buildBasicOperandVariable((HAPDefinitionOperandVariable)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_OPERATION)) {
			out = buildOperandOperation((HAPDefinitionOperandOperation)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)) {
			out = buildOperandReference((HAPDefinitionOperandReference)operandDef);
		}
		
		return out;
	}
	
	private static HAPBasicOperandAttribute buildBasicOperandAttribute(HAPDefinitionOperandAttribute operandDef) {
		HAPBasicOperandAttribute out = new HAPBasicOperandAttribute(operandDef);
		out.setBase(buildBasicOperand(operandDef.getBase()));
		return out;
	}
	
	private static HAPBasicOperandConstant buildBasicOperandConstant(HAPDefinitionOperandConstant operandDef) {
		HAPBasicOperandConstant out = new HAPBasicOperandConstant(operandDef);
		return out;
	}
	
	private static HAPBasicOperandVariable buildBasicOperandVariable(HAPDefinitionOperandVariable operandDef) {
		HAPBasicOperandVariable out = new HAPBasicOperandVariable(operandDef);
		return out;
	}

	private static HAPBasicOperandOperation buildOperandOperation(HAPDefinitionOperandOperation operandDef) {
		HAPBasicOperandOperation out = new HAPBasicOperandOperation(operandDef);
		if(operandDef.getBase()!=null) {
			out.setBase(buildBasicOperand(operandDef.getBase()));
		}
		
		Map<String, HAPDefinitionOperand> parmsDef = operandDef.getParms();
		for(String parmName : parmsDef.keySet()) {
			out.setParm(parmName, buildBasicOperand(parmsDef.get(parmName)));
		}
		return out;
	}
	
	private static HAPBasicOperandReference buildOperandReference(HAPDefinitionOperandReference operandDef) {
		HAPBasicOperandReference out = new HAPBasicOperandReference(operandDef);
		
		Map<String, HAPDefinitionOperand> refMapping = operandDef.getMapping();
		for(String name : refMapping.keySet()) {
			out.addMapping(name, buildBasicOperand(refMapping.get(name)));
		}
		
		return out;
	}
	
	
	
	
	
	public static void processConstant(HAPBasicExpressionData dataExpression, Map<String, HAPDefinitionConstant> constantsDef) {
		HAPBasicUtilityOperand.traverseAllOperand(dataExpression.getOperandWrapper(), new HAPBasicHandlerOperand(){
			@Override
			public boolean processOperand(HAPBasicWrapperOperand operandWrapper, Object data) {
				String opType = operandWrapper.getOperandType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)){
					HAPBasicOperandConstant constantOperand = (HAPBasicOperandConstant)operandWrapper.getOperand();
					if(constantOperand.getData()==null) {
						HAPData constantData = constantsDef.get(constantOperand.getName()).getData();
						constantOperand.setData(constantData);
					}
				}
				return true;
			}
		}, null);
	}
	
	public static void resolveReferenceVariableMapping(HAPBasicExpressionData dataExpression, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPBasicUtilityOperand.traverseAllOperand(dataExpression.getOperandWrapper(), new HAPBasicHandlerOperand(){
			@Override
			public boolean processOperand(HAPBasicWrapperOperand operandWrapper, Object data) {
				String opType = operandWrapper.getOperandType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
					HAPBasicOperandReference referenceOperand = (HAPBasicOperandReference)operandWrapper.getOperand();
					HAPDefinitionOperandReference referenceOperandDef = (HAPDefinitionOperandReference)referenceOperand.getOperandDefinition();

					HAPResourceId refResourceId = HAPFactoryResourceId.newInstance(referenceOperandDef.getReference());
					referenceOperand.setResourceId(refResourceId);
					HAPResourceDataBrick resourceData = HAPUtilityResource.getResourceData(refResourceId, resourceMan, runtimeInfo);
					HAPBrick referedResourceBrick = resourceData.getBrick();
					
					Map<String, HAPOperand> referenceMapping = referenceOperand.getMapping();
					for(String varName : referenceMapping.keySet()) {
						HAPInfoElementResolve varInfo = HAPUtilityResovleElement.resolveNameFromExternal(varName, HAPConstantShared.IO_DIRECTION_IN, referedResourceBrick, null, resourceData.getValueStructureDomain());
						HAPElementStructure eleStructure = varInfo.getElementStructure();
						String eleType = eleStructure.getType();
						if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
							HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)eleStructure;
							referenceOperand.addResolvedVariable(varName, varInfo.getElementId(), dataEle.getCriteria());
						} else {
							throw new RuntimeException();
						}
					}
				}
				return true;
			}
		}, null);
	}
	
	public static void resolveVariable(HAPBasicExpressionData dataExpression, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure) {
		HAPBasicUtilityOperand.traverseAllOperand(dataExpression.getOperandWrapper(), new HAPBasicHandlerOperand() {
			@Override
			public boolean processOperand(HAPBasicWrapperOperand operand, Object data) {
				String opType = operand.getOperandType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
					HAPBasicOperandVariable variableOperand = (HAPBasicOperandVariable)operand.getOperand();
					String variableKey = varInfoContainer.addVariable(variableOperand.getVariableName(), HAPConstantShared.IO_DIRECTION_OUT, resolveConfigure);
					variableOperand.setVariableKey(variableKey);
				}
				return true;
			}
		}, null);
	}
}
