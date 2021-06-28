package com.nosliw.data.core.activity.plugin.temp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPProcessorScript;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPSwitchActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id, 
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe,
			HAPValueStructureDefinitionGroup processDataContext, 
			Map<String, HAPExecutableDataAssociation> processResults,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker) {
		
		HAPSwitchActivityDefinition switchActDef = (HAPSwitchActivityDefinition)activityDefinition;
		HAPSwitchActivityExecutable out = new HAPSwitchActivityExecutable(id, switchActDef);

		//process input and create flat input context for activity
		HAPUtilityProcess.processBranchActivityInputDataAssocation(out, switchActDef, processDataContext, runtimeEnv);
		HAPValueStructureDefinitionFlat activityContext = (HAPValueStructureDefinitionFlat)out.getInputDataAssociation().getOutput().getOutputStructure(); 
		
		//process script expression defined in activity
		HAPUtilityProcess.buildScriptExpressionProcessContext(activityContext, out.getScriptExpressionProcessContext());
		HAPScriptExpression scriptExpression = HAPProcessorScript.processScriptExpression(switchActDef.getActivitySuiite(), out.getScriptExpressionProcessContext(), HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv.getExpressionManager(), runtimeEnv.getRuntime());
		out.setScriptExpression(scriptExpression);

		//merge discovered variable in activity back to process variable
		Map<String, HAPVariableInfo> affectedActivityVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Set<String> relatedVarNames = out.getScriptExpression().getDataVariableNames();  //all the data variables used in script expression
		for(String varName : relatedVarNames) 	affectedActivityVariablesInfo.put(varName, out.getScriptExpressionProcessContext().getDataVariables().get(varName));
		HAPUtilityProcess.mergeDataVariableInActivityToProcessContext(affectedActivityVariablesInfo, activityContext, processDataContext);

		//process branch
		HAPUtilityProcess.processBranchActivityBranch(out, switchActDef);
		
		return out;
	}
}
