package com.nosliw.data.core.domain.entity.expression.script;

import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPConstantInScript;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionData;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionDataScript;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionText;
import com.nosliw.core.application.common.scriptexpression.HAPVariableInScript;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;

public class HAPUtilityScriptExpressionExecute {

	public static HAPExpressionScript processExpression(HAPDefinitionExpression expressionDef, HAPExecutableEntityExpressionScript expressionEntity, HAPContextProcessor processContext, HAPGeneratorId idGenerator) {
		HAPExpressionScript out = new HAPExpressionScript(expressionDef.getType());

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
	
	private static void buildDataExpressionInExpression(HAPExpressionScript expressionExe) {
		List<HAPSegmentScriptExpression> segments = expressionExe.getSegments();
		for(HAPSegmentScriptExpression segment : segments) {
			collectDataExpressionId(segment, expressionExe.getDataExpressionIds());
		}
	}

	private static void collectDataExpressionId(HAPSegmentScriptExpression segment, Set<String> expressionIds) {
		if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
			HAPSegmentScriptExpressionData dataSegment = (HAPSegmentScriptExpressionData)segment;
			expressionIds.add(dataSegment.getDataExpressionId());
		}
		else if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
			HAPSegmentScriptExpressionDataScript dataScriptSegment = (HAPSegmentScriptExpressionDataScript)segment;
			for(HAPSegmentScriptExpression s : dataScriptSegment.getSegments()) {
				collectDataExpressionId(s, expressionIds);
			}
		}
	}
	
	private static void buildVariableInfoInExpression(HAPExpressionScript expressionExe) {
		List<HAPSegmentScriptExpression> segments = expressionExe.getSegments();
		for(HAPSegmentScriptExpression segment : segments) {
			collectVariableKeys(segment, expressionExe.getVariableKeys());
		}
	}
	
	private static void collectVariableKeys(HAPSegmentScriptExpression segment, Set<String> varKeys) {
		if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
			HAPSegmentScriptExpressionScript scriptSegment = (HAPSegmentScriptExpressionScript)segment;
			for(Object s : scriptSegment.getParts()) {
				if(s instanceof HAPVariableInScript) {
					HAPVariableInScript varInScript = (HAPVariableInScript)s;
					varKeys.add(varInScript.getVariableKey());
				}
			}
		}
		else if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
			HAPSegmentScriptExpressionDataScript dataScriptSegment = (HAPSegmentScriptExpressionDataScript)segment;
			for(HAPSegmentScriptExpression s : dataScriptSegment.getSegments()) {
				collectVariableKeys(s, varKeys);
			}
		}
	}
	
	private static HAPSegmentScriptExpressionText processSegmentText(HAPDefinitionSegmentExpressionText textSegDef, HAPGeneratorId idGenerator) {
		return new HAPSegmentScriptExpressionText(generateSegmentId(textSegDef.getType(), idGenerator), textSegDef.getContent());
	}
	
	private static HAPSegmentScriptExpressionData processSegmentData(HAPDefinitionSegmentExpressionData dataSegDef, HAPGeneratorId idGenerator) {
		return new HAPSegmentScriptExpressionData(generateSegmentId(dataSegDef.getType(), idGenerator), dataSegDef.getDataExpressionId());
	}
	
	private static HAPSegmentScriptExpressionDataScript processSegmentDataScript(HAPDefinitionSegmentExpressionDataScript dataScriptSegDef, HAPExecutableEntityExpressionScript expressionEntity, HAPContextProcessor processContext, HAPGeneratorId idGenerator) {
		
		HAPSegmentScriptExpressionDataScript out = new HAPSegmentScriptExpressionDataScript(generateSegmentId(dataScriptSegDef.getType(), idGenerator));
		
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
	
	private static HAPSegmentScriptExpressionScript processSegmentScript(HAPDefinitionSegmentExpressionScript scriptSegDef, HAPExecutableEntityExpressionScript expressionEntity, HAPContextProcessor processContext, HAPGeneratorId idGenerator) {
		HAPSegmentScriptExpressionScript out = new HAPSegmentScriptExpressionScript(generateSegmentId(scriptSegDef.getType(), idGenerator));
		for(Object segObj : scriptSegDef.getSegments()) {
			if(segObj instanceof String) {
				out.addPart(segObj);
			}
			else if(segObj instanceof HAPDefinitionConstantInScript) {
				HAPDefinitionConstantInScript constantSegDef = (HAPDefinitionConstantInScript)segObj;
				String contantName = constantSegDef.getConstantName();
				Object constantValue =  expressionEntity.getConstantValue(contantName);
				out.addPart(new HAPConstantInScript(contantName, constantValue));
			}
			else if(segObj instanceof HAPDefinitionVariableInScript) {
				HAPDefinitionVariableInScript varSegDef = (HAPDefinitionVariableInScript)segObj;
				HAPIdElement varId = HAPUtilityValueContextReference.resolveVariableName(varSegDef.getVariableName(), expressionEntity.getValueContext(), null, processContext.getCurrentValueStructureDomain(), null);
				String varKey = expressionEntity.getVariablesInfo().addVariable(varId);
				out.addPart(new HAPVariableInScript(varSegDef.getVariableName(), varKey));
			}
		}
		return out;
	}
	
	private static String generateSegmentId(String segType, HAPGeneratorId idGenerator) {
		return segType + "_" + idGenerator.generateId();
	}
}
