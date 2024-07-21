package com.nosliw.data.core.activity.plugin;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.data.core.activity.HAPBuilderResultContext;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.activity.HAPExecutableResultActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.activity.HAPUtilityActivity;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.expression.data1.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression1.HAPProcessorScript;

public class HAPExpressionActivityProcessor implements HAPProcessorActivity{

	private HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(); 
	 
	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPContextProcessor processContext, 
			HAPManualBrickWrapperValueStructure valueStructureWrapper,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorValueStructure configure, 
			HAPProcessTracker processTracker) {
		 
		HAPExpressionActivityDefinition definition = (HAPExpressionActivityDefinition)activityDefinition;
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, definition);
		
		//process script expression defined in activity
		HAPExecutableScriptGroup scriptExpressionGroup = HAPProcessorScript.processSimpleScript(definition.getScript().getScript().getScript(), definition.getScript().getScript().getType(), definition.getInputValueStructureWrapper(), null, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, processTracker);
		out.setScriptExpression(scriptExpressionGroup);

		//process input
		out.setInputDataAssociation(HAPUtilityActivity.processActivityInputDataAssocation(definition, valueStructureWrapper.getValueStructureBlock(), runtimeEnv));
		
		//process success result
		HAPExecutableResultActivity successResultExe = HAPUtilityActivity.processActivityResult(out, definition, HAPConstantShared.ACTIVITY_RESULT_SUCCESS, valueStructureWrapper.getValueStructureBlock(), m_resultContextBuilder, runtimeEnv);
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}

	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
//		@Override
//		public HAPValueStructureInValuePort buildResultValueStructure(String resultName, HAPExecutableActivity activity) {
//			HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
//			if(HAPConstantShared.ACTIVITY_RESULT_SUCCESS.equals(resultName)) {
//				String outputVar = HAPConstantShared.ACTIVITY_OUTPUTVARIABLE_OUTPUT;
//				HAPExpressionActivityExecutable expressionActExt = (HAPExpressionActivityExecutable)activity;
//				HAPExecutableScriptGroup scriptExpressionGroup = expressionActExt.getScriptExpression();
//				HAPExecutableScriptEntity scriptExpression = scriptExpressionGroup.getScript(null);
//				if(scriptExpression.isDataExpression()) {
//					//if script expression is data expression only, then affect result
//					HAPExpressionScript expExe = scriptExpressionGroup.getExpression().getExpressionItems().values().iterator().next();
//					HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
//					out.addRoot(HAPSystemUtility.buildSystemName(outputVar), new HAPElementStructureLeafData(new HAPVariableDataInfo(outputCriteria)));
//				}
//				else {
//					out.addRoot(HAPSystemUtility.buildSystemName(outputVar), new HAPElementStructureLeafValue());
//				}
//			}
//			return out;
//		}
	}

}
