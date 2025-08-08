package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPItemInContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.dataexpressionimp.HAPManualExpressionData;
import com.nosliw.core.application.common.dataexpressionimp.HAPManualUtilityProcessorDataExpression;
import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;

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
							constantInScript.setValue(constantsDef.get(constantInScript.getConstantName()).getValue());
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
	
	public static void fromDefToExeScriptExpressionContainer(HAPDefinitionContainerScriptExpression groupDef, HAPContainerScriptExpression groupExe, HAPParserDataExpression dataExpressionParser) {
		for(HAPDefinitionScriptExpressionItemInContainer itemDef : groupDef.getItems()) {
			HAPItemInContainerScriptExpression itemExe = new HAPItemInContainerScriptExpression();
			itemDef.cloneToEntityInfo(itemExe);

			HAPManualExpressionScript scriptExpression = HAPManualUtilityScriptExpressionParser.parseDefinitionExpression(itemDef.getScriptExpression().getScriptExpression(), itemDef.getScriptExpression().getScriptExpressionType(), dataExpressionParser);
			itemExe.setScriptExpression(scriptExpression);
			groupExe.addItem(itemExe);
		}
	}

	//variable resolve in script expression container
	public static void processScriptExpressionContainerVariableResolve(HAPContainerScriptExpression groupExe, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure, HAPManagerWithVariablePlugin withVariableMan) {
		for(HAPItemInContainerScriptExpression itemExe : groupExe.getItems()) {
			//variable resolve
			HAPUtilityWithVarible.resolveVariable(itemExe.getScriptExpression(), varInfoContainer, resolveConfigure, withVariableMan);
			//build variable info in script expression
			HAPUtilityWithVarible.buildVariableInfoInEntity(itemExe.getScriptExpression(), varInfoContainer, withVariableMan);
		}
	}
	
}
