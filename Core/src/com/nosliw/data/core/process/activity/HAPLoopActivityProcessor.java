package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
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
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPUtilityContext;
import com.nosliw.data.core.structure.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.structure.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPLoopActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id, 
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe,
			HAPStructureValueDefinitionGroup processDataContext, 
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
		HAPStructureValueDefinitionGroup stepDataContext = processDataContext.cloneContextGroup();
		
		//find container data criteria 
		HAPInfoReferenceResolve containerResolve = HAPUtilityContext.resolveReferencedContextElement(new HAPReferenceElement(loopActivityDef.getContainerName()), processDataContext, null, null);
		out.setContainerDataPath(containerResolve.path);
		HAPDataTypeCriteria containerCriteria = ((HAPElementLeafData)containerResolve.resolvedNode.getSolidStructureElement()).getCriteria();
		//find element data criteria from coontainer data criteria
		HAPDataTypeCriteria elementCriteria = HAPCriteriaUtility.getChildCriteriaByPath(containerCriteria, "element");
		//build element data context 
		stepDataContext.addElement(loopActivityDef.getElementName(), new HAPRoot(new HAPElementLeafData(new HAPVariableDataInfo(elementCriteria))), HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
		
		//index data definition
		HAPExecutableWrapperTask<HAPExecutableProcess> stepProcessExe = processManager.getEmbededProcess(
				loopActivityDef.getStep().getTaskDefinition(),
				processContext,
				loopActivityDef.getStep().getInputMapping(), 
				loopActivityDef.getStep().getOutputMapping(),
				HAPParentContext.createDefault(stepDataContext), 
				HAPParentContext.createDefault(stepDataContext)
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
		public HAPStructureValueDefinition buildResultContext(String resultName, HAPExecutableActivityNormal activity) {
			HAPLoopActivityExecutable processActivity = (HAPLoopActivityExecutable)activity;
			return this.m_processExe.getContext();
		}
	}
}
