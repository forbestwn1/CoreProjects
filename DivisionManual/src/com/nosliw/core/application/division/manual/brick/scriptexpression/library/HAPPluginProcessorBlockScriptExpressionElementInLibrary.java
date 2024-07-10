package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.scriptexpression.library.HAPBlockScriptExpressionElementInLibrary;
import com.nosliw.core.application.common.dataexpression.HAPElementInContainerDataExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.scriptexpression.HAPElementInLibraryScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionDataScript;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPVariableInScript;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPContainerVariableInfo;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPWithInternalValuePort;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPUtilityExpressionProcessor;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPUtilityScriptExpressionParser;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPPluginProcessorBlockScriptExpressionElementInLibrary extends HAPPluginProcessorBlockSimple{

	public HAPPluginProcessorBlockScriptExpressionElementInLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100);
	}

	@Override
	public void process(HAPManualBrick blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		HAPElementInLibraryScriptExpression exe = ((HAPBlockScriptExpressionElementInLibrary)blockExe).getValue();;
		HAPManualScriptExpressionLibraryElement def = ((HAPManualBlockScriptExpressionElementInLibrary)blockDef).getValue();
		
		//entity info
		def.cloneToEntityInfo(exe);
		
		//set interactive
		exe.setInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
		
		//expression
		HAPExpressionScript scriptExpression = HAPUtilityScriptExpressionParser.parseDefinitionExpression(def.getExpression(), null, exe.getDataExpressions(), processContext.getRuntimeEnv().getDataExpressionParser());
		exe.setExpression(scriptExpression);
		
		//process expression group
		HAPContainerVariableInfo currentVarInfoContainer = exe.getVariablesInfo();
		for(HAPElementInContainerDataExpression item : exe.getDataExpressions().getItems()) {
			Pair<HAPContainerVariableInfo, HAPMatchers> pair = HAPUtilityExpressionProcessor.processDataExpression(item.getExpression(), null, currentVarInfoContainer, blockExe, processContext.getRuntimeEnv());
			currentVarInfoContainer = pair.getLeft();
		}
		exe.setVariablesInfo(currentVarInfoContainer);

		//resolve variable in script
		List<HAPSegmentScriptExpression> segments = scriptExpression.getSegments();
		for(HAPSegmentScriptExpression segment : segments) {
			collectVariableKeys(segment, exe.getVariablesInfo(), blockExe, null);
		}
		
		//collect all variables in script expression
		
		
		
		//collection all expressions in script expression
		
		
	}
	
	private static void collectVariableKeys(HAPSegmentScriptExpression segment, HAPContainerVariableInfo varInfoContainer, HAPWithInternalValuePort withInternalValuePort, HAPConfigureResolveElementReference resolveConfigure) {
		if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
			HAPSegmentScriptExpressionScript scriptSegment = (HAPSegmentScriptExpressionScript)segment;
			for(Object s : scriptSegment.getParts()) {
				if(s instanceof HAPVariableInScript) {
					HAPVariableInScript varInScript = (HAPVariableInScript)s;
					HAPIdElement idVariable = HAPUtilityStructureElementReference.resolveNameFromInternal(varInScript.getVariableName(), HAPConstantShared.IO_DIRECTION_OUT, resolveConfigure, withInternalValuePort).getElementId();
					String variableKey = varInfoContainer.addVariable(idVariable);
					varInScript.setVariableKey(variableKey);
				}
			}
		}
		else if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
			HAPSegmentScriptExpressionDataScript dataScriptSegment = (HAPSegmentScriptExpressionDataScript)segment;
			for(HAPSegmentScriptExpression s : dataScriptSegment.getChildren()) {
				collectVariableKeys(s, varInfoContainer, withInternalValuePort, resolveConfigure);
			}
		}
	}

}
