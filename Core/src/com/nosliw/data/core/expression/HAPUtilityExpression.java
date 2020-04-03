package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.updatename.HAPUpdateNamePrefix;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElement;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceUtility;

public class HAPUtilityExpression {

	public static HAPUpdateName getUpdateExpressionVariableName(HAPExecutableExpression expression) {
		return new HAPUpdateNamePrefix(expression.getId()+"_");
	}
	
	public static String getBeforeUpdateName(HAPExecutableExpression expression, String name) {
		return name.substring((expression.getId()+"_").length());
	}

	public static void normalizeReference(HAPDefinitionExpressionSuite expressionSuite) {
		Map<String, HAPResourceDefinitionContainerElement> elements = expressionSuite.getAllElements();
		for(String name : elements.keySet()) {
			HAPResourceDefinitionContainerElement element = elements.get(name);
			if(element.getType().equals(HAPResourceDefinitionContainerElement.TYPE_ENTITY)) {
				HAPDefinitionExpressionSuiteElementEntity expressionEle = (HAPDefinitionExpressionSuiteElementEntity)element;
				Map<String, HAPDefinitionExpressionSuiteElementEntityExpression> expressions = expressionEle.getExpressions();
				for(String expName : expressions.keySet()) {
					normalizeReference(expressions.get(expName));
				}
			}
		}
	}
	
	public static void normalizeReference(HAPDefinitionExpressionSuiteElementEntityExpression expressionEntity) {
		HAPOperandUtility.processAllOperand(expressionEntity.getOperand(), null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					String refName = referenceOperand.getReferenceName();
					HAPDefinitionReference refDef = expressionEntity.getReference(refName);
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
						
						expressionEntity.addReference(refDef);
					}
				}
				return true;
			}
		});
	}
}
