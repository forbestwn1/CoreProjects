package com.nosliw.data.core.domain.entity.expression.script;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;

public class HAPUtilityExpressionExecute {

	public HAPExecutableExpression processExpression(HAPDefinitionExpression expressionDef, Map<String, Object> constantValues,  HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain) {
		HAPExecutableExpression out = null;

		String expressionType = expressionDef.getType();
		if(expressionType.equals(HAPConstantShared.EXPRESSION_TYPE_TEXT)) 	out = new HAPExecutableExpressionText();
		else if(expressionType.equals(HAPConstantShared.EXPRESSION_TYPE_LITERATE)) out = new HAPExecutableExpressionLiterate();
		if(expressionType.equals(HAPConstantShared.EXPRESSION_TYPE_SCRIPT)) out = new HAPExecutableExpressionScript();
		
		for(HAPDefinitionSegmentExpression segDef : expressionDef.getSegments()) {
			String segType = segDef.getType();
			if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT)) {
				out.addSegment(processSegmentText((HAPDefinitionSegmentExpressionText)segDef));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
				out.addSegment(processSegmentScript((HAPDefinitionSegmentExpressionScript)segDef, constantValues, valueContext, valueStructureDomain));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
				out.addSegment(processSegmentData((HAPDefinitionSegmentExpressionData)segDef));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
				out.addSegment(processSegmentDataScript((HAPDefinitionSegmentExpressionDataScript)segDef, constantValues, valueContext, valueStructureDomain));
			}
		}
		return out;
	}
	
	
	private HAPExecutableSegmentExpressionText processSegmentText(HAPDefinitionSegmentExpressionText textSegDef) {
		return new HAPExecutableSegmentExpressionText(textSegDef.getContent());
	}
	
	private HAPExecutableSegmentExpressionData processSegmentData(HAPDefinitionSegmentExpressionData dataSegDef) {
		return new HAPExecutableSegmentExpressionData(dataSegDef.getDataExpressionId());
	}
	
	private HAPExecutableSegmentExpressionDataScript processSegmentDataScript(HAPDefinitionSegmentExpressionDataScript dataScriptSegDef, Map<String, Object> constantValues,  HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain) {
		
		HAPExecutableSegmentExpressionDataScript out = new HAPExecutableSegmentExpressionDataScript();
		
		for(HAPDefinitionSegmentExpression segDef : dataScriptSegDef.getSegments()) {
			String segType = segDef.getType();
			if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
				out.addSegmentScript(processSegmentScript((HAPDefinitionSegmentExpressionScript)segDef, constantValues,  valueContext, valueStructureDomain));
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
				out.addSegmentData(processSegmentData((HAPDefinitionSegmentExpressionData)segDef));
			}
		}
		return out;
	}
	
	private HAPExecutableSegmentExpressionScript processSegmentScript(HAPDefinitionSegmentExpressionScript scriptSegDef, Map<String, Object> constantValues,  HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain) {
		HAPExecutableSegmentExpressionScript out = new HAPExecutableSegmentExpressionScript();
		for(Object segObj : scriptSegDef.getSegments()) {
			if(segObj instanceof String) {
				out.addSegment(segObj);
			}
			else if(segObj instanceof HAPDefinitionConstantInScript) {
				HAPDefinitionConstantInScript constantSegDef = (HAPDefinitionConstantInScript)segObj;
				String contantName = constantSegDef.getConstantName();
				out.addSegment(new HAPExecutableConstantInScript(contantName, constantValues.get(contantName)));
			}
			else if(segObj instanceof HAPDefinitionVariableInScript) {
				HAPDefinitionVariableInScript varSegDef = (HAPDefinitionVariableInScript)segObj;
				HAPIdVariable varId = HAPUtilityValueContextReference.resolveVariableName(varSegDef.getVariableName(), valueContext, null, valueStructureDomain, null);
				out.addSegment(new HAPExecutableVariableInScript(varSegDef.getVariableName(), varId));
			}
		}
		return out;
	}
	
}
