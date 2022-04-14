package com.nosliw.data.core.activity.plugin;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPBuilderResultContext;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.activity.HAPExecutableResultActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.activity.HAPUtilityActivity;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableExpression;
import com.nosliw.data.core.domain.entity.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPExecutableScriptEntity;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression.HAPProcessorScript;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafValue;
import com.nosliw.data.core.system.HAPSystemUtility;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPExpressionActivityProcessor implements HAPProcessorActivity{

	private HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(); 
	 
	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPContextProcessor processContext, 
			HAPValueStructureWrapper valueStructureWrapper,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker) {
		 
		HAPExpressionActivityDefinition definition = (HAPExpressionActivityDefinition)activityDefinition;
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, definition);
		
		//process script expression defined in activity
		HAPExecutableScriptGroup scriptExpressionGroup = HAPProcessorScript.processSimpleScript(definition.getScript().getScript().getScript(), definition.getScript().getScript().getType(), definition.getInputValueStructureWrapper(), null, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, processTracker);
		out.setScriptExpression(scriptExpressionGroup);

		//process input
		out.setInputDataAssociation(HAPUtilityActivity.processActivityInputDataAssocation(definition, valueStructureWrapper.getValueStructure(), runtimeEnv));
		
		//process success result
		HAPExecutableResultActivity successResultExe = HAPUtilityActivity.processActivityResult(out, definition, HAPConstantShared.ACTIVITY_RESULT_SUCCESS, valueStructureWrapper.getValueStructure(), m_resultContextBuilder, runtimeEnv);
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}

	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		@Override
		public HAPValueStructure buildResultValueStructure(String resultName, HAPExecutableActivity activity) {
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
					out.addRoot(HAPSystemUtility.buildSystemName(outputVar), new HAPElementStructureLeafData(new HAPVariableDataInfo(outputCriteria)));
				}
				else {
					out.addRoot(HAPSystemUtility.buildSystemName(outputVar), new HAPElementStructureLeafValue());
				}
			}
			return out;
		}
	}

}
