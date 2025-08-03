package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPItemInContainerDataExpression;
import com.nosliw.core.application.common.dataexpressionimp.HAPUtilityExpressionProcessor;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPManualUtilityScriptExpressionParser;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.valueport.HAPWithInternalValuePort;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.xxx.application.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.xxx.application.valueport.HAPIdElement;
import com.nosliw.core.xxx.application.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.scriptexpression.library.HAPBlockScriptExpressionElementInLibrary;
import com.nosliw.core.xxx.application1.brick.scriptexpression.library.HAPElementInLibraryScriptExpression;
import com.nosliw.core.xxx.application1.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.xxx.application1.common.scriptexpression.HAPSegmentScriptExpressionScriptComplex;
import com.nosliw.core.xxx.application1.common.scriptexpression.HAPSegmentScriptExpressionScriptScriptSimple;
import com.nosliw.core.xxx.application1.common.scriptexpression.HAPVariableInScript;
import com.nosliw.core.xxx.application1.division.manual.definition.HAPManualDefinitionBrickBlockSimple;

public class HAPPluginProcessorBlockScriptExpressionElementInLibrary extends HAPManualPluginProcessorBlockSimple{

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
		HAPExpressionScript scriptExpression = HAPManualUtilityScriptExpressionParser.parseDefinitionExpression(def.getExpression(), null, exe.getDataExpressions(), processContext.getRuntimeEnv().getDataExpressionParser());
		exe.setExpression(scriptExpression);
		
		//process expression group
		HAPContainerVariableInfo currentVarInfoContainer = exe.getVariablesInfo();
		for(HAPItemInContainerDataExpression item : exe.getDataExpressions().getItems()) {
			Pair<HAPContainerVariableInfo, HAPMatchers> pair = HAPUtilityExpressionProcessor.processDataExpression(item.getDataExpression(), null, currentVarInfoContainer, blockExe, processContext.getRuntimeEnv());
			currentVarInfoContainer = pair.getLeft();
		}
		exe.setVariablesInfo(currentVarInfoContainer);

		//resolve variable in script
		List<HAPManualSegmentScriptExpression> segments = scriptExpression.getSegments();
		for(HAPManualSegmentScriptExpression segment : segments) {
			collectVariableKeys(segment, exe.getVariablesInfo(), blockExe, null);
		}
		
		//collect all variables in script expression
		
		
		
		//collection all expressions in script expression
		
		
	}
	
	private static void collectVariableKeys(HAPManualSegmentScriptExpression segment, HAPContainerVariableInfo varInfoContainer, HAPWithInternalValuePort withInternalValuePort, HAPConfigureResolveElementReference resolveConfigure) {
		if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
			HAPManualSegmentScriptExpressionScriptSimple scriptSegment = (HAPManualSegmentScriptExpressionScriptSimple)segment;
			for(Object s : scriptSegment.getParts()) {
				if(s instanceof HAPManualVariableInScript) {
					HAPManualVariableInScript varInScript = (HAPManualVariableInScript)s;
					HAPIdElement idVariable = HAPUtilityStructureElementReference.resolveNameFromInternal(varInScript.getVariableName(), HAPConstantShared.IO_DIRECTION_OUT, resolveConfigure, withInternalValuePort).getElementId();
					String variableKey = varInfoContainer.addVariable(idVariable);
					varInScript.setVariableKey(variableKey);
				}
			}
		}
		else if(segment.getType().equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTCOMPLEX)) {
			HAPManualSegmentScriptExpressionScriptComplex dataScriptSegment = (HAPManualSegmentScriptExpressionScriptComplex)segment;
			for(HAPManualSegmentScriptExpression s : dataScriptSegment.getChildren()) {
				collectVariableKeys(s, varInfoContainer, withInternalValuePort, resolveConfigure);
			}
		}
	}

}
