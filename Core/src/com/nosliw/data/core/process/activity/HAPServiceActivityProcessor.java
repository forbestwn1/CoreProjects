package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionResultActivityNormal;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;

public class HAPServiceActivityProcessor implements HAPProcessorActivity{

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
		
		HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
		serviceUseDef.setProvider(serviceActDef.getProvider());
		serviceUseDef.setParmMapping(serviceActDef.getParmMapping());
		
		Map<String, HAPDefinitionResultActivityNormal> resultsDef = serviceActDef.getResults();
		for(String resultName : resultsDef.keySet()) {
			serviceUseDef.addResultMapping(resultName, resultsDef.get(resultName).getOutputDataAssociation());
		}
		
		HAPExecutableServiceUse serviceUseExe = HAPProcessorServiceUse.process(serviceUseDef, serviceProviders.get(serviceUseDef.getProvider()).getServiceInterface(), context, configure, contextProcessRequirement);
		out.setService(serviceUseExe);
		
		return out;
	}
}
