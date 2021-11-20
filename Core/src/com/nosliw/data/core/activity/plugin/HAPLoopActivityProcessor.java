package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
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
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPReferenceElementInStructure;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPLoopActivityProcessor implements HAPProcessorActivity{

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
		 
		HAPLoopActivityDefinition loopActivityDef = (HAPLoopActivityDefinition)activityDefinition;
		HAPLoopActivityExecutable out = new HAPLoopActivityExecutable(id, loopActivityDef);
		
		//input
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, loopActivityDef, processDataContext, runtimeEnv);

		//build data context for step process (context from process + element data)
		HAPValueStructureDefinitionGroup stepDataContext = processDataContext.cloneValueStructureGroup();
		
		//find container data criteria 
		HAPInfoReferenceResolve containerResolve = HAPUtilityContext.analyzeElementReference(new HAPReferenceElementInStructure(loopActivityDef.getContainerName()), processDataContext, null, null);
		out.setContainerDataPath(containerResolve.path);
		HAPDataTypeCriteria containerCriteria = ((HAPElementStructureLeafData)containerResolve.resolvedElement.getSolidStructureElement()).getCriteria();
		//find element data criteria from coontainer data criteria
		HAPDataTypeCriteria elementCriteria = HAPUtilityCriteria.getChildCriteriaByPath(containerCriteria, "element");
		//build element data context 
		stepDataContext.addRoot(loopActivityDef.getElementName(), new HAPRootStructure(new HAPElementStructureLeafData(new HAPVariableDataInfo(elementCriteria))), HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
		
		//index data definition
		HAPExecutableWrapperTask<HAPExecutableProcess> stepProcessExe = processManager.getEmbededProcess(
				loopActivityDef.getStep().getTaskDefinition(),
				processContext,
				loopActivityDef.getStep().getInDataAssociation(), 
				loopActivityDef.getStep().getOutDataAssociations(),
				HAPContainerStructure.createDefault(stepDataContext), 
				HAPContainerStructure.createDefault(stepDataContext)
		);
		out.setStep(stepProcessExe);
		
		//process success result
		for(String resultName : loopActivityDef.getResults().keySet()) {
			HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, loopActivityDef, resultName, processDataContext, new HAPBuilderResultContext1(processExe), runtimeEnv);
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
			HAPLoopActivityExecutable processActivity = (HAPLoopActivityExecutable)activity;
			return this.m_processExe.getContext();
		}
	}
}
