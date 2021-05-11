package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPOutputStructure;
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
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPServiceActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe,
			HAPValueStructureDefinitionGroup processDataContext,
			Map<String, HAPExecutableDataAssociation> results,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRuntimeEnvironment runtimeEnv, 
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker) {
		HAPServiceActivityDefinition serviceActDef = (HAPServiceActivityDefinition)activityDefinition;
		HAPServiceActivityExecutable out = new HAPServiceActivityExecutable(id, serviceActDef);

		//input
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, serviceActDef, processDataContext, runtimeEnv);

		//provider
		HAPDefinitionServiceProvider provider = serviceProviders.get(serviceActDef.getProvider());
		out.setServiceProvider(provider.getServiceId());
		
		//prepare service use def
		HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
		serviceUseDef.setProvider(serviceActDef.getProvider());
		serviceUseDef.setServiceMapping(serviceActDef.getTaskMapping());
		
		//process service use def
		HAPExecutableServiceUse serviceUseExe = HAPProcessorServiceUse.process(serviceUseDef, provider.getServiceInterface(), processDataContext, configure, runtimeEnv);
		out.setService(serviceUseExe);

		//process success result
		HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(processDataContext); 
		HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, serviceActDef, HAPConstantShared.ACTIVITY_RESULT_SUCCESS, processDataContext, m_resultContextBuilder, runtimeEnv);
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}
	
	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		
		HAPValueStructureDefinitionGroup m_processContext;
		
		public HAPBuilderResultContext1(HAPValueStructureDefinitionGroup processContext) {
			this.m_processContext = processContext;
		}
		
		@Override
		public HAPValueStructureDefinition buildResultContext(String resultName, HAPExecutableActivityNormal activity) {
			HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
			if(HAPConstantShared.ACTIVITY_RESULT_SUCCESS.equals(resultName)) {
				HAPServiceActivityExecutable serviceActivity = (HAPServiceActivityExecutable)activity;
				HAPExecutableDataAssociation outputMapping = serviceActivity.getService().getServiceMapping().getOutputMapping(resultName);
				HAPOutputStructure outputStructure = outputMapping.getOutput();
				return outputStructure.getOutputStructure();
			}
			return out;
		}
	}
}
