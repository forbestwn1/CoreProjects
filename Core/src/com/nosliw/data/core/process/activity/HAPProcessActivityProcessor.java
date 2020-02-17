package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
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
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public class HAPProcessActivityProcessor implements HAPProcessorActivity{

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
		 
		HAPProcessActivityDefinition processActivityDef = (HAPProcessActivityDefinition)activityDefinition;
		HAPProcessActivityExecutable out = new HAPProcessActivityExecutable(id, processActivityDef);
		
		//input
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, processDataContext, contextProcessRequirement);

		HAPExecutableWrapperTask<HAPExecutableProcess> emProcessExe = processManager.getEmbededProcess(
				processActivityDef.getProcess(),
				processContext,
				processActivityDef.getTaskMapping().getInputMapping(), 
				processActivityDef.getTaskMapping().getOutputMapping(),
				HAPParentContext.createDefault(processDataContext), 
				HAPParentContext.createDefault(processDataContext)
		);
		out.setProcess(emProcessExe);
		
		//process success result
		HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(processExe); 
		
		for(String resultName : emProcessExe.getTask().getResultNames()) {
			HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, resultName, processDataContext, m_resultContextBuilder, contextProcessRequirement);
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
		public HAPContextStructure buildResultContext(String resultName, HAPExecutableActivityNormal activity) {
			HAPProcessActivityExecutable processActivity = (HAPProcessActivityExecutable)activity;
			return this.m_processExe.getContext();
		}
	}
}
