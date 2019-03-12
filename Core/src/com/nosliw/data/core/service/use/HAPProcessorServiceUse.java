package com.nosliw.data.core.service.use;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;

public class HAPProcessorServiceUse {

	public static HAPExecutableServiceUse process(
			HAPDefinitionServiceUse definition,
			HAPServiceInterface providerInterface,
			HAPContextStructure globalContext, 
			HAPConfigureContextProcessor configure, 
			HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableServiceUse out = new HAPExecutableServiceUse(definition);
		
		HAPDefinitionMappingService serviceMapping = definition.getServiceMapping();
		
		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyStructureFalse(new HAPInfoImpSimple());
		//process parm mapping
		HAPExecutableDataAssociation processedParms = HAPProcessorDataAssociation.processDataAssociation(
				HAPParentContext.createDefault(globalContext), 
				serviceMapping.getParms(), 
				HAPParentContext.createDefault(HAPUtilityServiceUse.buildContextFromServiceParms(providerInterface)), 
				daConfigure, 
				contextProcessRequirement);
		out.setParmMapping(processedParms);
		 
		//process result mapping
		Map<String, HAPDefinitionDataAssociation> resultMapping = serviceMapping.getResultMapping();
		for(String result :resultMapping.keySet()) {
			HAPContext outputContext = HAPUtilityServiceUse.buildContextFromResultServiceOutputs(providerInterface, result); 
			HAPExecutableDataAssociation processedResult = HAPProcessorDataAssociation.processDataAssociation(
					HAPParentContext.createDefault(outputContext), 
					resultMapping.get(result),
					HAPParentContext.createDefault(globalContext), 
					daConfigure, 
					contextProcessRequirement);
			out.addResultMapping(result, processedResult);
		}
		return out;
	}
	
}
