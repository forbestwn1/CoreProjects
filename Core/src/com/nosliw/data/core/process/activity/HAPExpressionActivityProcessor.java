package com.nosliw.data.core.process.activity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.process.HAPBuilderResultContext;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPExecutableResultActivityNormal;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPExecutableExpression;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafValue;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public class HAPExpressionActivityProcessor implements HAPProcessorActivity{

	private HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(); 
	 
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
		 
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, (HAPExpressionActivityDefinition)activityDefinition);

		//process input and create flat input context for activity
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, processDataContext, contextProcessRequirement);
		HAPContext activityContext = (HAPContext)out.getInputDataAssociation().getOutput().getOutputStructure(); 
		
		//process script expression defined in activity
		HAPUtilityProcess.buildScriptExpressionProcessContext(activityContext, out.getScriptExpressionProcessContext());
		HAPScriptExpression scriptExpression = HAPProcessorScriptExpression.processScriptExpression(out.getExpressionActivityDefinition().getExpression(), out.getScriptExpressionProcessContext(), HAPExpressionProcessConfigureUtil.setDoDiscovery(null), contextProcessRequirement.expressionManager, contextProcessRequirement.runtime);
		out.setScriptExpression(scriptExpression);

		//merge discovered variable in activity back to process variable
		Map<String, HAPVariableInfo> affectedActivityVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Set<String> relatedVarNames = out.getScriptExpression().getDataVariableNames();  //all the data variables used in script expression
		for(String varName : relatedVarNames) 	affectedActivityVariablesInfo.put(varName, out.getScriptExpressionProcessContext().getDataVariables().get(varName));
		HAPUtilityProcess.mergeDataVariableInActivityToProcessContext(affectedActivityVariablesInfo, activityContext, processDataContext);

		//process success result
		HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, HAPConstant.ACTIVITY_RESULT_SUCCESS, processDataContext, m_resultContextBuilder, contextProcessRequirement);
		out.addResult(HAPConstant.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}

	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		@Override
		public HAPContextStructure buildResultContext(String resultName, HAPExecutableActivityNormal activity) {
			HAPContext out = new HAPContext();
			if(HAPConstant.ACTIVITY_RESULT_SUCCESS.equals(resultName)) {
				String outputVar = HAPConstant.ACTIVITY_OUTPUTVARIABLE_OUTPUT;
				HAPExpressionActivityExecutable expressionActExt = (HAPExpressionActivityExecutable)activity;
				HAPScriptExpression scriptExpression = expressionActExt.getScriptExpression();
				if(scriptExpression.isDataExpression()) {
					//if script expression is data expression only, then affect result
					HAPExecutableExpression expExe = scriptExpression.getExpressions().values().iterator().next();
					HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
					out.addElement(HAPUtilityProcess.buildOutputVarialbeName(outputVar), new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(outputCriteria)));
				}
				else {
					out.addElement(HAPUtilityProcess.buildOutputVarialbeName(outputVar), new HAPContextDefinitionLeafValue());
				}
			}
			return out;
		}
	}
}
