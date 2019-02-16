package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.process.HAPBuilderResultContext;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionResultActivityNormal;
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
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.script.expression.HAPScriptExpression;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;

public class HAPServiceActivityProcessor implements HAPProcessorActivity{

	private HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(); 

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPExecutableProcess processExe,
			HAPContextGroup context,
			Map<String, HAPExecutableDataAssociationGroup> results,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions, 
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRequirementContextProcessor contextProcessRequirement, 
			HAPConfigureContextProcessor configure, 
			HAPProcessTracker processTracker) {
		HAPServiceActivityDefinition serviceActDef = (HAPServiceActivityDefinition)activityDefinition;
		HAPServiceActivityExecutable out = new HAPServiceActivityExecutable(id, serviceActDef);
		
		//provider
		HAPDefinitionServiceProvider provider = serviceProviders.get(serviceActDef.getProvider());
		out.setServiceProvider(provider);
		
		//prepare service use def
		HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
		serviceUseDef.setProvider(serviceActDef.getProvider());
		serviceUseDef.setParmMapping(serviceActDef.getParmMapping());
		
		Map<String, HAPDefinitionResultActivityNormal> resultsDef = serviceActDef.getResults();
		for(String resultName : resultsDef.keySet()) {
			serviceUseDef.addResultMapping(resultName, resultsDef.get(resultName).getOutputDataAssociation());
		}
		//process service use def
		HAPExecutableServiceUse serviceUseExe = HAPProcessorServiceUse.process(serviceUseDef, provider.getServiceInterface(), context, configure, contextProcessRequirement);
		out.setService(serviceUseExe);

		//process success result
		HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, HAPConstant.ACTIVITY_RESULT_SUCCESS, context, m_resultContextBuilder, contextProcessRequirement);
		out.addResult(HAPConstant.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}
	
	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		@Override
		public HAPContext buildResultContext(String resultName, HAPExecutableActivityNormal activity) {
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
