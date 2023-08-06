package com.nosliw.data.core.domain.entity.expression.script;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;

public class HAPUtilityExpressionExecute {

	public static HAPExecutableExpression processExpression(HAPDefinitionExpression expressionDef, Map<String, Object> constantValues, HAPContainerVariableCriteriaInfo variableContainer, HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain, HAPGeneratorId idGenerator) {
		HAPExecutableExpression out = new HAPExecutableExpression(expressionDef.getType());

		expressionDef.cloneToEntityInfo(out);
		
		for(HAPDefinitionSegmentExpression segDef : expressionDef.getSegments()) {
			String segType = segDef.getType();
			if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT)) {
				out.addSegment(processSegmentText((HAPDefinitionSegmentExpressionText)segDef, idGenerator));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
				out.addSegment(processSegmentScript((HAPDefinitionSegmentExpressionScript)segDef, constantValues, variableContainer, valueContext, valueStructureDomain, idGenerator));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
				out.addSegment(processSegmentData((HAPDefinitionSegmentExpressionData)segDef, idGenerator));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
				out.addSegment(processSegmentDataScript((HAPDefinitionSegmentExpressionDataScript)segDef, constantValues, variableContainer, valueContext, valueStructureDomain, idGenerator));
			}
		}
		return out;
	}
	
	private static HAPExecutableSegmentExpressionText processSegmentText(HAPDefinitionSegmentExpressionText textSegDef, HAPGeneratorId idGenerator) {
		return new HAPExecutableSegmentExpressionText(idGenerator.generateId(), textSegDef.getContent());
	}
	
	private static HAPExecutableSegmentExpressionData processSegmentData(HAPDefinitionSegmentExpressionData dataSegDef, HAPGeneratorId idGenerator) {
		return new HAPExecutableSegmentExpressionData(idGenerator.generateId(), dataSegDef.getDataExpressionId());
	}
	
	private static HAPExecutableSegmentExpressionDataScript processSegmentDataScript(HAPDefinitionSegmentExpressionDataScript dataScriptSegDef, Map<String, Object> constantValues, HAPContainerVariableCriteriaInfo variableContainer,  HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain, HAPGeneratorId idGenerator) {
		
		HAPExecutableSegmentExpressionDataScript out = new HAPExecutableSegmentExpressionDataScript(idGenerator.generateId());
		
		for(HAPDefinitionSegmentExpression segDef : dataScriptSegDef.getSegments()) {
			String segType = segDef.getType();
			if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
				out.addSegmentScript(processSegmentScript((HAPDefinitionSegmentExpressionScript)segDef, constantValues, variableContainer, valueContext, valueStructureDomain, idGenerator));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
				out.addSegmentData(processSegmentData((HAPDefinitionSegmentExpressionData)segDef, idGenerator));
			}
		}
		return out;
	}
	
	private static HAPExecutableSegmentExpressionScript processSegmentScript(HAPDefinitionSegmentExpressionScript scriptSegDef, Map<String, Object> constantValues, HAPContainerVariableCriteriaInfo variableContainer,  HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain, HAPGeneratorId idGenerator) {
		HAPExecutableSegmentExpressionScript out = new HAPExecutableSegmentExpressionScript(idGenerator.generateId());
		for(Object segObj : scriptSegDef.getSegments()) {
			if(segObj instanceof String) {
				out.addPart(segObj);
			}
			else if(segObj instanceof HAPDefinitionConstantInScript) {
				HAPDefinitionConstantInScript constantSegDef = (HAPDefinitionConstantInScript)segObj;
				String contantName = constantSegDef.getConstantName();
				out.addPart(new HAPExecutableConstantInScript(contantName, constantValues.get(contantName)));
			}
			else if(segObj instanceof HAPDefinitionVariableInScript) {
				HAPDefinitionVariableInScript varSegDef = (HAPDefinitionVariableInScript)segObj;
				HAPIdVariable varId = HAPUtilityValueContextReference.resolveVariableName(varSegDef.getVariableName(), valueContext, null, valueStructureDomain, null);
				String varKey = variableContainer.addVariable(varId);
				out.addPart(new HAPExecutableVariableInScript(varSegDef.getVariableName(), varKey));
			}
		}
		return out;
	}
}
