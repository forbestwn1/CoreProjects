package com.nosliw.data.core.activity.plugin;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPBuilderResultContext;
import com.nosliw.data.core.activity.HAPContextProcessAttachmentReferenceActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.activity.HAPExecutableResultActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity1;
import com.nosliw.data.core.activity.HAPUtilityActivity;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPExecutableScriptEntity;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression.HAPProcessorScript;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafValue;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPExpressionActivityProcessor1 implements HAPProcessorActivity1{

	private HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(); 
	 
	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPContextProcessAttachmentReferenceActivity processContext, 
			HAPWrapperValueStructure valueStructureWrapper,
			Map<String, HAPExecutableDataAssociation> results, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker) {
		 
		HAPExpressionActivityDefinition definition = (HAPExpressionActivityDefinition)activityDefinition;
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, definition);
		
		//process input and create flat input context for activity
		HAPUtilityActivity.processNormalActivityInputDataAssocation(out, definition, valueStructureWrapper.getValueStructure(), runtimeEnv);
		
		
		HAPValueStructureDefinitionFlat activityContext = (HAPValueStructureDefinitionFlat)out.getInputDataAssociation().getOutput().getOutputStructure(); 
		
		//process script expression defined in activity
		HAPUtilityProcess.buildScriptExpressionProcessContext(activityContext, out.getScriptExpressionProcessContext());
//		HAPScriptExpression scriptExpression = HAPProcessorScript.processScriptExpression(definition.getScript(), out.getScriptExpressionProcessContext(), HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv.getExpressionManager(), runtimeEnv.getRuntime());
		HAPExecutableScriptGroup scriptExpressionGroup = HAPProcessorScript.processSimpleScript(definition.getScript().getScript().getScript(), definition.getScript().getScript().getType(), valueStructureWrapper, null, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, processTracker);
		out.setScriptExpression(scriptExpressionGroup);

		//merge discovered variable in activity back to process variable
		Set<HAPVariableInfo> affectedActivityVariablesInfo = HAPUtilityScriptExpression.getDataVariables(out.getScriptExpression(), null); 
//		HAPUtilityProcess.mergeDataVariableInActivityToProcessContext(affectedActivityVariablesInfo, activityContext, processDataContext);

		//process success result
		HAPExecutableResultActivity successResultExe = HAPUtilityActivity.processNormalActivityResult(out, definition, HAPConstantShared.ACTIVITY_RESULT_SUCCESS, valueStructureWrapper.getValueStructure(), m_resultContextBuilder, runtimeEnv);
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}

	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		@Override
		public HAPValueStructure buildResultContext(String resultName, HAPExecutableActivity activity) {
			HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
			if(HAPConstantShared.ACTIVITY_RESULT_SUCCESS.equals(resultName)) {
				String outputVar = HAPConstantShared.ACTIVITY_OUTPUTVARIABLE_OUTPUT;
				HAPExpressionActivityExecutable expressionActExt = (HAPExpressionActivityExecutable)activity;
				HAPExecutableScriptGroup scriptExpressionGroup = expressionActExt.getScriptExpression();
				HAPExecutableScriptEntity scriptExpression = scriptExpressionGroup.getScript(null);
				if(scriptExpression.isDataExpression()) {
					//if script expression is data expression only, then affect result
					HAPExecutableExpression expExe = scriptExpressionGroup.getExpression().getExpressionItems().values().iterator().next();
					HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
					out.addRoot(HAPUtilityProcess.buildOutputVarialbeName(outputVar), new HAPElementStructureLeafData(new HAPVariableDataInfo(outputCriteria)));
				}
				else {
					out.addRoot(HAPUtilityProcess.buildOutputVarialbeName(outputVar), new HAPElementStructureLeafValue());
				}
			}
			return out;
		}
	}

}
