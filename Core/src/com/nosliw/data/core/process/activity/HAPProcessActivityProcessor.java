package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.process.HAPBuilderResultContext;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPExecutableResultActivityNormal;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public class HAPProcessActivityProcessor implements HAPProcessorActivity{

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
			HAPManagerProcessDefinition processManager,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPConfigureContextProcessor configure, 
			HAPProcessTracker processTracker) {
		 
		HAPProcessActivityDefinition processActivityDef = (HAPProcessActivityDefinition)activityDefinition;
		HAPProcessActivityExecutable out = new HAPProcessActivityExecutable(id, processActivityDef);
		
		//input
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, processDataContext, contextProcessRequirement);

		HAPExecutableWrapperTask emProcessExe = processManager.getEmbededProcess(
				processActivityDef.getProcess(),
				processContext,
				processActivityDef.getTaskMapping().getInputMapping(), 
				processActivityDef.getTaskMapping().getOutputMapping(),
				HAPParentContext.createDefault(processDataContext), 
				null
		);
		out.setProcess(emProcessExe);
		
		//process input and create flat input context for activity
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, processDataContext, contextProcessRequirement);
		HAPContext activityContext = (HAPContext)out.getInputDataAssociation().getOutput().getOutputStructure(); 
		
		//process success result
		HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, HAPConstant.ACTIVITY_RESULT_SUCCESS, processDataContext, m_resultContextBuilder, contextProcessRequirement);
		out.addResult(HAPConstant.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}

	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		@Override
		public HAPContextStructure buildResultContext(String resultName, HAPExecutableActivityNormal activity) {
			HAPContextGroup out = new HAPContextGroup();
			if(HAPConstant.ACTIVITY_RESULT_SUCCESS.equals(resultName)) {
				HAPProcessActivityExecutable processActivity = (HAPProcessActivityExecutable)activity;
				return processActivity.getProcess().getTask().getOutResultContext().get(resultName).getContext();
			}
			return out;
		}
	}
}
