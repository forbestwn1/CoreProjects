package com.nosliw.data.core.activity.plugin;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.division.manual.HAPManualEntityComplex;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityWrapperValueStructure;
import com.nosliw.data.core.activity.HAPBuilderResultContext;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.activity.HAPExecutableResultActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.activity.HAPUtilityActivity;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;

public class HAPServiceActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPContextProcessor processContext, 
			HAPDefinitionEntityWrapperValueStructure valueStructureWrapper,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorValueStructure configure, 
			HAPProcessTracker processTracker) {
		HAPServiceActivityDefinition serviceActDef = (HAPServiceActivityDefinition)activityDefinition;
		HAPServiceActivityExecutable out = new HAPServiceActivityExecutable(id, serviceActDef);

		HAPDefinitionServiceUse serviceUse = serviceActDef.getServiceUse();
		if(serviceUse==null) {
			//service use is reference
			HAPManualEntityComplex complexEntity = processContext.getComplexEntity();
			if(complexEntity instanceof HAPDefinitionEntityComponent) {
				serviceUse = ((HAPDefinitionEntityComponent)complexEntity).getService(serviceActDef.getServiceUseName());
			}
		}
		
		HAPProcessorServiceUse.normalizeServiceUse(serviceUse, processContext.getComplexEntity().getAttachmentContainer(), runtimeEnv);
		HAPProcessorServiceUse.enhanceValueStructureByService(serviceUse, serviceActDef.getInputValueStructureWrapper().getValueStructure(), runtimeEnv);

		HAPExecutableServiceUse serviceExe = HAPProcessorServiceUse.process(serviceUse, valueStructureWrapper.getValueStructure(), processContext.getComplexEntity().getAttachmentContainer(), runtimeEnv);
		out.setService(serviceExe);
		
		//process input
		out.setInputDataAssociation(HAPUtilityActivity.processActivityInputDataAssocation(serviceActDef, valueStructureWrapper.getValueStructure(), runtimeEnv));

		//process success result
		HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(serviceActDef.getInputValueStructureWrapper().getValueStructure()); 
		HAPExecutableResultActivity successResultExe = HAPUtilityActivity.processActivityResult(out, serviceActDef, HAPConstantShared.ACTIVITY_RESULT_SUCCESS, valueStructureWrapper.getValueStructure(), m_resultContextBuilder, runtimeEnv);
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}
	
	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		
//		HAPValueStructure m_activityValueStructure;
//		
//		public HAPBuilderResultContext1(HAPValueStructure activityValueStructure) {
//			this.m_activityValueStructure = activityValueStructure;
//		}
//		
//		@Override
//		public HAPValueStructure buildResultValueStructure(String resultName, HAPExecutableActivity activity) {
//			HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
//			if(HAPConstantShared.ACTIVITY_RESULT_SUCCESS.equals(resultName)) {
//				return this.m_activityValueStructure;
//			}
//			return out;
//		}
	}
}
