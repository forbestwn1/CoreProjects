package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
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
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPProcessActivityProcessor implements HAPProcessorActivity{

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
		 
		HAPProcessActivityDefinition processActivityDef = (HAPProcessActivityDefinition)activityDefinition;
		HAPProcessActivityExecutable out = new HAPProcessActivityExecutable(id, processActivityDef);
		
		//input
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, processActivityDef, processDataContext, runtimeEnv);

		HAPExecutableWrapperTask<HAPExecutableProcess> emProcessExe = processManager.getEmbededProcess(
				processActivityDef.getProcess(),
				processContext,
				processActivityDef.getTaskMapping().getInputMapping(), 
				processActivityDef.getTaskMapping().getOutputMapping(),
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
			HAPProcessActivityExecutable processActivity = (HAPProcessActivityExecutable)activity;
			return this.m_processExe.getContext();
		}
	}
}
