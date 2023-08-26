package com.nosliw.data.core.domain.entity.expression.script;

import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.entity.HAPUtilityComplexConstant;

public class HAPUtilityScriptExpressionExecute {

	public static HAPExecutableExpression processExpression(HAPDefinitionExpression expressionDef, HAPExecutableEntityExpressionScript expressionEntity, HAPContextProcessor processContext, HAPGeneratorId idGenerator) {
		HAPExecutableExpression out = new HAPExecutableExpression(expressionDef.getType());

		expressionDef.cloneToEntityInfo(out);
		
		for(HAPDefinitionSegmentExpression segDef : expressionDef.getSegments()) {
			String segType = segDef.getType();
			if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT)) {
				out.addSegment(processSegmentText((HAPDefinitionSegmentExpressionText)segDef, idGenerator));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
				out.addSegment(processSegmentScript((HAPDefinitionSegmentExpressionScript)segDef, expressionEntity, processContext, idGenerator));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
				out.addSegment(processSegmentData((HAPDefinitionSegmentExpressionData)segDef, idGenerator));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
				out.addSegment(processSegmentDataScript((HAPDefinitionSegmentExpressionDataScript)segDef, expressionEntity, processContext, idGenerator));
			}
		}
		
		buildVariableInfoInExpression(out);
		buildDataExpressionInExpression(out);
		return out;

	}
	
	private static void buildDataExpressionInExpression(HAPExecutableExpression expressionExe) {
		List<HAPExecutableSegmentExpression> segments = expressionExe.getSegments();
		for(HAPExecutableSegmentExpression segment : segments) {
			collectDataExpressionId(segment, expressionExe.getDataExpressionIds());
		}
	}

	private static void collectDataExpressionId(HAPExecutableSegmentExpression segment, Set<String> expressionIds) {
		if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
			HAPExecutableSegmentExpressionData dataSegment = (HAPExecutableSegmentExpressionData)segment;
			expressionIds.add(dataSegment.getDataExpressionId());
		}
		else if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
			HAPExecutableSegmentExpressionDataScript dataScriptSegment = (HAPExecutableSegmentExpressionDataScript)segment;
			for(HAPExecutableSegmentExpression s : dataScriptSegment.getSegments()) {
				collectDataExpressionId(s, expressionIds);
			}
		}
	}
	
	private static void buildVariableInfoInExpression(HAPExecutableExpression expressionExe) {
		List<HAPExecutableSegmentExpression> segments = expressionExe.getSegments();
		for(HAPExecutableSegmentExpression segment : segments) {
			collectVariableKeys(segment, expressionExe.getVariableKeys());
		}
	}
	
	private static void collectVariableKeys(HAPExecutableSegmentExpression segment, Set<String> varKeys) {
		if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
			HAPExecutableSegmentExpressionScript scriptSegment = (HAPExecutableSegmentExpressionScript)segment;
			for(Object s : scriptSegment.getParts()) {
				if(s instanceof HAPExecutableVariableInScript) {
					HAPExecutableVariableInScript varInScript = (HAPExecutableVariableInScript)s;
					varKeys.add(varInScript.getVariableKey());
				}
			}
		}
		else if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
			HAPExecutableSegmentExpressionDataScript dataScriptSegment = (HAPExecutableSegmentExpressionDataScript)segment;
			for(HAPExecutableSegmentExpression s : dataScriptSegment.getSegments()) {
				collectVariableKeys(s, varKeys);
			}
		}
	}
	
	private static HAPExecutableSegmentExpressionText processSegmentText(HAPDefinitionSegmentExpressionText textSegDef, HAPGeneratorId idGenerator) {
		return new HAPExecutableSegmentExpressionText(generateSegmentId(textSegDef.getType(), idGenerator), textSegDef.getContent());
	}
	
	private static HAPExecutableSegmentExpressionData processSegmentData(HAPDefinitionSegmentExpressionData dataSegDef, HAPGeneratorId idGenerator) {
		return new HAPExecutableSegmentExpressionData(generateSegmentId(dataSegDef.getType(), idGenerator), dataSegDef.getDataExpressionId());
	}
	
	private static HAPExecutableSegmentExpressionDataScript processSegmentDataScript(HAPDefinitionSegmentExpressionDataScript dataScriptSegDef, HAPExecutableEntityExpressionScript expressionEntity, HAPContextProcessor processContext, HAPGeneratorId idGenerator) {
		
		HAPExecutableSegmentExpressionDataScript out = new HAPExecutableSegmentExpressionDataScript(generateSegmentId(dataScriptSegDef.getType(), idGenerator));
		
		for(HAPDefinitionSegmentExpression segDef : dataScriptSegDef.getSegments()) {
			String segType = segDef.getType();
			if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
				out.addSegmentScript(processSegmentScript((HAPDefinitionSegmentExpressionScript)segDef, expressionEntity, processContext, idGenerator));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
				out.addSegmentData(processSegmentData((HAPDefinitionSegmentExpressionData)segDef, idGenerator));
			}
		}
		return out;
	}
	
	private static HAPExecutableSegmentExpressionScript processSegmentScript(HAPDefinitionSegmentExpressionScript scriptSegDef, HAPExecutableEntityExpressionScript expressionEntity, HAPContextProcessor processContext, HAPGeneratorId idGenerator) {
		HAPExecutableSegmentExpressionScript out = new HAPExecutableSegmentExpressionScript(generateSegmentId(scriptSegDef.getType(), idGenerator));
		for(Object segObj : scriptSegDef.getSegments()) {
			if(segObj instanceof String) {
				out.addPart(segObj);
			}
			else if(segObj instanceof HAPDefinitionConstantInScript) {
				HAPDefinitionConstantInScript constantSegDef = (HAPDefinitionConstantInScript)segObj;
				String contantName = constantSegDef.getConstantName();
				Object constantValue = HAPUtilityComplexConstant.getConstantValue(contantName, expressionEntity, processContext);
				out.addPart(new HAPExecutableConstantInScript(contantName, constantValue));
			}
			else if(segObj instanceof HAPDefinitionVariableInScript) {
				HAPDefinitionVariableInScript varSegDef = (HAPDefinitionVariableInScript)segObj;
				HAPIdVariable varId = HAPUtilityValueContextReference.resolveVariableName(varSegDef.getVariableName(), expressionEntity.getValueContext(), null, processContext.getCurrentValueStructureDomain(), null);
				String varKey = expressionEntity.getVariablesInfo().addVariable(varId);
				out.addPart(new HAPExecutableVariableInScript(varSegDef.getVariableName(), varKey));
			}
		}
		return out;
	}
	
	private static String generateSegmentId(String segType, HAPGeneratorId idGenerator) {
		return segType + "_" + idGenerator.generateId();
	}
}
