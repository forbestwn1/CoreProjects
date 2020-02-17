package com.nosliw.data.core.process.activity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public class HAPSwitchActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id, 
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe,
			HAPContextGroup processDataContext, 
			Map<String, HAPExecutableDataAssociation> processResults,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPConfigureContextProcessor configure, 
			HAPProcessTracker processTracker) {
		 
		HAPSwitchActivityExecutable out = new HAPSwitchActivityExecutable(id, (HAPSwitchActivityDefinition)activityDefinition);

		//process input and create flat input context for activity
		HAPUtilityProcess.processBranchActivityInputDataAssocation(out, processDataContext, contextProcessRequirement);
		HAPContext activityContext = (HAPContext)out.getInputDataAssociation().getOutput().getOutputStructure(); 
		
		//process script expression defined in activity
		HAPUtilityProcess.buildScriptExpressionProcessContext(activityContext, out.getScriptExpressionProcessContext());
		HAPScriptExpression scriptExpression = HAPProcessorScriptExpression.processScriptExpression(out.getSwitchActivityDefinition().getExpression(), out.getScriptExpressionProcessContext(), HAPExpressionProcessConfigureUtil.setDoDiscovery(null), contextProcessRequirement.expressionManager, contextProcessRequirement.runtime);
		out.setScriptExpression(scriptExpression);

		//merge discovered variable in activity back to process variable
		Map<String, HAPVariableInfo> affectedActivityVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Set<String> relatedVarNames = out.getScriptExpression().getDataVariableNames();  //all the data variables used in script expression
		for(String varName : relatedVarNames) 	affectedActivityVariablesInfo.put(varName, out.getScriptExpressionProcessContext().getDataVariables().get(varName));
		HAPUtilityProcess.mergeDataVariableInActivityToProcessContext(affectedActivityVariablesInfo, activityContext, processDataContext);

		//process branch
		HAPUtilityProcess.processBranchActivityBranch(out);
		
		return out;
	}
}
