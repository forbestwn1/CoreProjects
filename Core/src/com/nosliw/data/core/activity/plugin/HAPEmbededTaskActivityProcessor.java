package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.process1.HAPBuilderResultContext;
import com.nosliw.data.core.process1.HAPContextProcessor;
import com.nosliw.data.core.process1.HAPExecutableActivity;
import com.nosliw.data.core.process1.HAPExecutableActivityNormal;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.process1.HAPExecutableResultActivityNormal;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPEmbededTaskActivityProcessor implements HAPProcessorActivity{

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
		 
		HAPEmbededTaskActivityDefinition processActivityDef = (HAPEmbededTaskActivityDefinition)activityDefinition;
		HAPEmbededTaskActivityExecutable out = new HAPEmbededTaskActivityExecutable(id, processActivityDef);
		
		//input
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, processActivityDef, processDataContext, runtimeEnv);

		HAPExecutableWrapperTask<HAPExecutableProcess> emProcessExe = processManager.getEmbededProcess(
				processActivityDef.getProcess(),
				processContext,
				processActivityDef.getTaskMapping().getInDataAssociation(), 
				processActivityDef.getTaskMapping().getOutDataAssociations(),
				HAPContainerStructure.createDefault(processDataContext), 
				HAPContainerStructure.createDefault(processDataContext)
		);
		out.setProcess(emProcessExe);
		
		//process success result
		HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(processExe); 
		
		for(String resultName : emProcessExe.getTask().getResultNames()) {
			HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, processActivityDef, resultName, processDataContext, m_resultContextBuilder, runtimeEnv);
			out.addResult(resultName, successResultExe);
		}
		return out;
	}

	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		
		private HAPExecutableProcess m_processExe;

		public HAPBuilderResultContext1(HAPExecutableProcess processExe) {
			this.m_processExe = processExe;
		}
		
		@Override
		public HAPValueStructure buildResultContext(String resultName, HAPExecutableActivityNormal activity) {
			HAPEmbededTaskActivityExecutable processActivity = (HAPEmbededTaskActivityExecutable)activity;
			return this.m_processExe.getContext();
		}
	}
}
