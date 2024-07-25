package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPItemInContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.scriptexpression.HAPContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPItemInContainerScriptExpression;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualExpressionData;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualUtilityProcessorDataExpression;

public class HAPManualUtilityScriptExpression {

	public static void processScriptExpressionConstant(HAPManualExpressionScript scriptExpression, Map<String, HAPDefinitionConstant> constantsDef) {
		//script it self
		HAPManualUtilityScriptExpressionTraverse.traverse(scriptExpression, new HAPManualProcessorScriptExpressionSegment() {
			@Override
			public boolean process(HAPManualSegmentScriptExpression segment, Object value) {
				Set<String> varKeys = (Set<String>)value;
				String segType = segment.getType();
				if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
					HAPManualSegmentScriptExpressionScriptSimple simpleScriptSeg = (HAPManualSegmentScriptExpressionScriptSimple)segment;
					for(Object part : simpleScriptSeg.getParts()) {
						if(part instanceof HAPManualConstantInScript) {
							HAPManualConstantInScript constantInScript = (HAPManualConstantInScript)part;
							constantInScript.setValue(constantsDef.get(constantInScript.getConstantName()));
						}
					}
				}
				return true;
			}
		}, null);
		
		//data expression
		HAPContainerDataExpression dataExpressions = scriptExpression.getDataExpressionContainer();
		for(HAPItemInContainerDataExpression item : dataExpressions.getItems()) {
			HAPManualUtilityProcessorDataExpression.processConstant((HAPManualExpressionData)item.getDataExpression(), constantsDef);
		}
	}
	
	public static void fromDefToExeScriptExpressionContainer(HAPManualDefinitionContainerScriptExpression groupDef, HAPContainerScriptExpression groupExe, HAPParserDataExpression dataExpressionParser) {
		for(HAPManualDefinitionScriptExpressionItemInContainer itemDef : groupDef.getItems()) {
			HAPItemInContainerScriptExpression itemExe = new HAPItemInContainerScriptExpression();
			itemDef.cloneToEntityInfo(itemExe);

			HAPManualExpressionScript scriptExpression = HAPManualUtilityScriptExpressionParser.parseDefinitionExpression(itemDef.getScriptExpression(), null, dataExpressionParser);
			itemExe.setScriptExpression(scriptExpression);
			groupExe.addItem(itemExe);
		}
	}

	//variable resolve in script expression container
	public static void processScriptExpressionContainerVariableResolve(HAPContainerScriptExpression groupExe, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure, HAPManualManagerBrick manualBrickMan) {
		for(HAPItemInContainerScriptExpression itemExe : groupExe.getItems()) {
			//variable resolve
			HAPUtilityWithVarible.resolveVariable(itemExe.getScriptExpression(), varInfoContainer, resolveConfigure, manualBrickMan);
			//build variable info in script expression
			HAPUtilityWithVarible.buildVariableInfoInEntity(itemExe.getScriptExpression(), varInfoContainer, manualBrickMan);
		}
	}
	
}
