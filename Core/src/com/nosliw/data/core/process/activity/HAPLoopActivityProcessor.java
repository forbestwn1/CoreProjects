package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
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
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public class HAPLoopActivityProcessor implements HAPProcessorActivity{

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
		 
		HAPLoopActivityDefinition loopActivityDef = (HAPLoopActivityDefinition)activityDefinition;
		HAPLoopActivityExecutable out = new HAPLoopActivityExecutable(id, loopActivityDef);
		
		//input
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, loopActivityDef, processDataContext, contextProcessRequirement);

		//build data context for step process (context from process + element data)
		HAPContextGroup stepDataContext = processDataContext.cloneContextGroup();
		
		//find container data criteria 
		HAPInfoRelativeContextResolve containerResolve = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(loopActivityDef.getContainerName()), processDataContext, null, null);
		out.setContainerDataPath(containerResolve.path);
		HAPDataTypeCriteria containerCriteria = ((HAPContextDefinitionLeafData)containerResolve.resolvedNode.getSolidContextDefinitionElement()).getCriteria();
		//find element data criteria from coontainer data criteria
		HAPDataTypeCriteria elementCriteria = HAPCriteriaUtility.getChildCriteriaByPath(containerCriteria, "element");
		//build element data context 
		stepDataContext.addElement(loopActivityDef.getElementName(), new HAPContextDefinitionRoot(new HAPContextDefinitionLeafData(new HAPVariableDataInfo(elementCriteria))), HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);
		
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
			HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, loopActivityDef, resultName, processDataContext, new HAPBuilderResultContext1(processExe), contextProcessRequirement);
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
			HAPLoopActivityExecutable processActivity = (HAPLoopActivityExecutable)activity;
			return this.m_processExe.getContext();
		}
	}
}
