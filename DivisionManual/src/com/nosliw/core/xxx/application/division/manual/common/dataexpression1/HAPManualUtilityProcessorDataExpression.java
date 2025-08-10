package com.nosliw.core.xxx.application.division.manual.common.dataexpression1;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
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
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPUtilityResource;
import com.nosliw.core.xxx.application.valueport.HAPInfoElementResolve;
import com.nosliw.core.xxx.application.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.xxx.application1.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualUtilityProcessorDataExpression {
	
	public static void resolveReferenceVariableMapping(HAPManualExpressionData dataExpression, HAPRuntimeEnvironment runtimEnv) {
		HAPManualUtilityOperand.traverseAllOperand(dataExpression.getOperandWrapper(), new HAPManualHandlerOperand(){
			@Override
			public boolean processOperand(HAPManualWrapperOperand operandWrapper, Object data) {
				String opType = operandWrapper.getOperandType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
					HAPManualOperandReference referenceOperand = (HAPManualOperandReference)operandWrapper.getOperand();
					HAPDefinitionOperandReference referenceOperandDef = (HAPDefinitionOperandReference)referenceOperand.getOperandDefinition();

					HAPResourceId refResourceId = HAPFactoryResourceId.newInstance(referenceOperandDef.getReference());
					referenceOperand.setResourceId(refResourceId);
					HAPBlockDataExpressionElementInLibrary brickResourceData = (HAPBlockDataExpressionElementInLibrary)HAPUtilityResource.getResourceDataBrick(refResourceId, runtimEnv.getResourceManager(), runtimEnv.getRuntime().getRuntimeInfo());
					
					Map<String, HAPOperand> referenceMapping = referenceOperand.getMapping();
					for(String varName : referenceMapping.keySet()) {
						HAPInfoElementResolve varInfo = HAPUtilityStructureElementReference.resolveNameFromExternal(varName, HAPConstantShared.IO_DIRECTION_IN, brickResourceData, null);
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
	
	public static void resolveVariable(HAPManualExpressionData dataExpression, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure) {
		HAPManualUtilityOperand.traverseAllOperand(dataExpression.getOperandWrapper(), new HAPManualHandlerOperand() {
			@Override
			public boolean processOperand(HAPManualWrapperOperand operand, Object data) {
				String opType = operand.getOperandType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
					HAPManualOperandVariable variableOperand = (HAPManualOperandVariable)operand.getOperand();
					String variableKey = varInfoContainer.addVariable(variableOperand.getVariableName(), HAPConstantShared.IO_DIRECTION_OUT, resolveConfigure);
					variableOperand.setVariableKey(variableKey);
				}
				return true;
			}
		}, null);
	}
	
	public static HAPManualExpressionData buildManualDataExpression(HAPDefinitionDataExpression dataExpressionDef) {
		return new HAPManualExpressionData(HAPManualUtilityProcessorDataExpression.buildManualOperand(dataExpressionDef.getOperand()));
	}
	
	public static HAPManualOperand buildManualOperand(HAPDefinitionOperand operandDef) {
		HAPManualOperand out = null;
		
		String operandType = operandDef.getType();
		
		if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)) {
			out = buildManualOperandAttribute((HAPDefinitionOperandAttribute)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)) {
			out = buildManualOperandConstant((HAPDefinitionOperandConstant)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)) {
			out = buildManualOperandVariable((HAPDefinitionOperandVariable)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_OPERATION)) {
			out = buildManualOperandOperation((HAPDefinitionOperandOperation)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)) {
			out = buildManualOperandReference((HAPDefinitionOperandReference)operandDef);
		}
		
		return out;
	}
	
	private static HAPManualOperandAttribute buildManualOperandAttribute(HAPDefinitionOperandAttribute operandDef) {
		HAPManualOperandAttribute out = new HAPManualOperandAttribute(operandDef);
		out.setBase(buildManualOperand(operandDef.getBase()));
		return out;
	}
	
	private static HAPManualOperandConstant buildManualOperandConstant(HAPDefinitionOperandConstant operandDef) {
		HAPManualOperandConstant out = new HAPManualOperandConstant(operandDef);
		return out;
	}
	
	private static HAPManualOperandVariable buildManualOperandVariable(HAPDefinitionOperandVariable operandDef) {
		HAPManualOperandVariable out = new HAPManualOperandVariable(operandDef);
		return out;
	}

	private static HAPManualOperandOperation buildManualOperandOperation(HAPDefinitionOperandOperation operandDef) {
		HAPManualOperandOperation out = new HAPManualOperandOperation(operandDef);
		if(operandDef.getBase()!=null) {
			out.setBase(buildManualOperand(operandDef.getBase()));
		}
		
		Map<String, HAPDefinitionOperand> parmsDef = operandDef.getParms();
		for(String parmName : parmsDef.keySet()) {
			out.setParm(parmName, buildManualOperand(parmsDef.get(parmName)));
		}
		return out;
	}
	
	private static HAPManualOperandReference buildManualOperandReference(HAPDefinitionOperandReference operandDef) {
		HAPManualOperandReference out = new HAPManualOperandReference(operandDef);
		
		Map<String, HAPDefinitionOperand> refMapping = operandDef.getMapping();
		for(String name : refMapping.keySet()) {
			out.addMapping(name, buildManualOperand(refMapping.get(name)));
		}
		
		return out;
	}

}
