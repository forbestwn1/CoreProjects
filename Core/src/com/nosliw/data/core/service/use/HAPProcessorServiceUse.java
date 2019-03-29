package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableTask;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
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
		
		HAPExecutableTask taskExe = new HAPExecutableTask() {
			@Override
			public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {		return HAPResourceDataFactory.createJSValueResourceData("");	}

			@Override
			public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {		return null;	}

			@Override
			public HAPParentContext getInContext() {
				return HAPParentContext.createDefault(HAPUtilityServiceUse.buildContextFromServiceParms(providerInterface));
			}

			@Override
			public Map<String, HAPParentContext> getOutResultContext() {
				Map<String, HAPParentContext> out = new LinkedHashMap<String, HAPParentContext>();
				Map<String, HAPContext> resultsContext = HAPUtilityServiceUse.buildContextFromResultServiceOutputs(providerInterface);
				for(String resultName : resultsContext.keySet()) {
					out.put(resultName, HAPParentContext.createDefault(resultsContext.get(resultName)));
				}
				return out;
			}
		};
		HAPExecutableWrapperTask serviceMappingExe = HAPProcessorDataAssociation.processDataAssociationWithTask(definition.getServiceMapping(), taskExe, HAPParentContext.createDefault(globalContext), null, contextProcessRequirement);
		out.setServiceMapping(serviceMappingExe);
		
//		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyStructureFalse(new HAPInfoImpSimple());
//		//process parm mapping
//		HAPExecutableDataAssociation processedParms = HAPProcessorDataAssociation.processDataAssociation(
//				HAPParentContext.createDefault(globalContext), 
//				serviceMapping.getParms(), 
//				HAPParentContext.createDefault(HAPUtilityServiceUse.buildContextFromServiceParms(providerInterface)), 
//				daConfigure, 
//				contextProcessRequirement);
//		out.setParmMapping(processedParms);
//		 
//		//process result mapping
//		Map<String, HAPDefinitionDataAssociation> resultMapping = serviceMapping.getOutputMapping();
//		for(String result :resultMapping.keySet()) {
//			HAPContext outputContext = HAPUtilityServiceUse.buildContextFromResultServiceOutputs(providerInterface, result); 
//			HAPExecutableDataAssociation processedResult = HAPProcessorDataAssociation.processDataAssociation(
//					HAPParentContext.createDefault(outputContext), 
//					resultMapping.get(result),
//					HAPParentContext.createDefault(globalContext), 
//					daConfigure, 
//					contextProcessRequirement);
//			out.addResultMapping(result, processedResult);
//		}
		return out;
	}
	
}
