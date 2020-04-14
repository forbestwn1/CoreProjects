package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.updatename.HAPUpdateNamePrefix;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPUtilityDataComponent;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithDataContext;
import com.nosliw.data.core.component.HAPComponentContainerElement;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceUtility;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityExpression {

	public static Map<String, HAPData> getDataConstants(HAPDefinitionExpressionGroup expressionGroupDef){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
		Map<String, HAPDefinitionConstant> cstDefs = getConstantsDefinition(expressionGroupDef);
		for(String id : cstDefs.keySet()) {
			HAPData data = cstDefs.get(id).getData();
			if(data!=null)	out.put(id, data);
		}
		return out;
	}
	
	public static Map<String, HAPDefinitionConstant> getConstantsDefinition(HAPDefinitionExpressionGroup expressionGroupDef){
		Map<String, HAPDefinitionConstant> out = null;
		if(expressionGroupDef instanceof HAPWithAttachment) {
			out = HAPUtilityDataComponent.buildDataConstantDefinition(((HAPWithAttachment)expressionGroupDef).getAttachmentContainer());
		}
		else {
			out = expressionGroupDef.getConstantDefinitions();
		}
		if(out==null)   throw new RuntimeException();
		return out;
	}

	public static HAPContext getContext(HAPDefinitionExpressionGroup expressionGroupDef, HAPContext extraContext, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPContext out = null;
		if(expressionGroupDef instanceof HAPComponentContainerElement) {
			out = (HAPContext)HAPUtilityComponent.processElementComponentContext((HAPComponentContainerElement)expressionGroupDef, extraContext, contextProcessRequirement, HAPUtilityExpressionProcessConfigure.getContextProcessConfigurationForExpression());
		}
		else {
			out = (HAPContext)HAPUtilityContext.hardMerge(((HAPWithDataContext)expressionGroupDef).getContextStructure(), extraContext); 
		}
		return out;
	}
	
	public static HAPUpdateName getUpdateExpressionVariableName(HAPExecutableExpressionGroup expression) {
		return new HAPUpdateNamePrefix(expression.getId()+"_");
	}
	
	public static String getBeforeUpdateName(HAPExecutableExpressionGroup expression, String name) {
		return name.substring((expression.getId()+"_").length());
	}

//	public static void normalizeReference(HAPDefinitionExpressionSuite expressionSuite) {
//		Map<String, HAPDefinitionExpressionGroup> elements = expressionSuite.getEntityElements();
//		for(String name : elements.keySet()) {
//			HAPDefinitionExpressionGroup element = elements.get(name);
//			HAPDefinitionResourceDefinitionExpressionSuiteElementEntity expressionEle = (HAPDefinitionResourceDefinitionExpressionSuiteElementEntity)element;
//			Map<String, HAPDefinitionExpression> expressions = expressionEle.getEntityElements();
//			for(String expName : expressions.keySet()) {
//				normalizeReference(expressions.get(expName));
//			}
//		}
//	}
	
//	public static void normalizeReference(HAPDefinitionExpressionGroup expressionDef) {
//		Map<String, HAPDefinitionExpression> elements = expressionDef.getEntityElements();
//		for(String name : elements.keySet()) {
//			HAPDefinitionExpression element = elements.get(name);
//			normalizeReference(element);
//		}
//	}
	
//	public static void normalizeReference(HAPDefinitionExpression expressionEntity) {
//		HAPOperandUtility.processAllOperand(expressionEntity.getOperand(), null, new HAPOperandTask(){
//			@Override
//			public boolean processOperand(HAPOperandWrapper operand, Object data) {
//				String opType = operand.getOperand().getType();
//				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
//					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
//					String refName = referenceOperand.getReferenceName();
//					HAPDefinitionReference refDef = expressionEntity.getReference(refName);
//					if(refDef==null) {
//						refDef = new HAPDefinitionReference();
//						String referenceTo = null;
//						String eleName = null;
//						String[] segs = HAPNamingConversionUtility.parsePaths(refName);
//						if(segs.length==1) {
//							referenceTo = segs[0];
//						}
//						else if(segs.length == 2) {
//							referenceTo = segs[0];
//							eleName = segs[1];
//						}
//						
//						refDef.setName(referenceTo);
//						refDef.setElementName(eleName);
//						refDef.setResourceId(HAPResourceUtility.buildLocalReferenceResourceId(refName));
//						refDef.setInputMapping(null);
//						
//						expressionEntity.addReference(refDef);
//					}
//				}
//				return true;
//			}
//		});
//	}

	public static Map<String, HAPDefinitionReference> normalizeReferenceDefinition(HAPOperandWrapper operand, Map<String, HAPDefinitionReference> referenceDef) {
		Map<String, HAPDefinitionReference> out = new LinkedHashMap<String, HAPDefinitionReference>();
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					String refName = referenceOperand.getReferenceName();
					HAPDefinitionReference refDef = referenceDef.get(refName);
					if(refDef==null) {
						refDef = new HAPDefinitionReference();
						String referenceTo = null;
						String eleName = null;
						String[] segs = HAPNamingConversionUtility.parsePaths(refName);
						if(segs.length==1) {
							referenceTo = segs[0];
						}
						else if(segs.length == 2) {
							referenceTo = segs[0];
							eleName = segs[1];
						}
						
						refDef.setName(referenceTo);
						refDef.setElementName(eleName);
						refDef.setResourceId(HAPResourceUtility.buildLocalReferenceResourceId(refName));
						refDef.setInputMapping(null);
					}
					out.put(refName, refDef);
				}
				return true;
			}
		});
		return out;
	}

}
