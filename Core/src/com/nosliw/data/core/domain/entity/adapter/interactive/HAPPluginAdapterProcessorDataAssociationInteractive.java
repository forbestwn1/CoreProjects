package com.nosliw.data.core.domain.entity.adapter.interactive;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginAdapterProcessor;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableGroupDataAssociationForTask;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.common.interactive.HAPExecutableEntityInteractive;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.interactive.HAPContextStructureReferenceInteractiveRequest;
import com.nosliw.data.core.interactive.HAPContextStructureReferenceInteractiveResult;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;
import com.nosliw.data.core.interactive.HAPDefinitionInteractiveResult;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPPluginAdapterProcessorDataAssociationInteractive implements HAPPluginAdapterProcessor{

	@Override
	public String getAdapterType() {  return HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONINTERACTIVE;  }

	@Override
	public Object process(Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext) {
		HAPExecutableGroupDataAssociationForTask out = new HAPExecutableGroupDataAssociationForTask();
		
		HAPDefinitionEntityDataAssociationInteractive adapterDef = HAPUtilityDomain.getEntity(adapter, parentContext, HAPDefinitionEntityDataAssociationInteractive.class);
		HAPDefinitionGroupDataAssociationForTask dataAssociationGroup = adapterDef.getDataAssociation();
		
		HAPExecutableEntityComplex parentComplexEntityExe = (HAPExecutableEntityComplex)parentEntityExecutable;
		
		HAPExecutableEntityInteractive interactiveEntityExe = (HAPExecutableEntityInteractive)childEntityExecutable;
		HAPDefinitionInteractive interactiveExe = interactiveEntityExe.getInteractive();
		
		//data association for request parms
		HAPExecutableDataAssociation inExe = HAPProcessorDataAssociation.processDataAssociation(
				dataAssociationGroup.getInDataAssociation(),
				new HAPContextStructureReferenceValueStructure(parentComplexEntityExe.getValueContext(), null, parentContext.getCurrentValueStructureDomain()),
				new HAPContextStructureReferenceInteractiveRequest(interactiveExe.getRequestParms()),
				parentContext.getRuntimeEnvironment());
		out.setInDataAssociation(inExe);

		//data association for result
		Map<String, HAPDefinitionInteractiveResult> results = interactiveExe.getResults();
		Map<String, HAPDefinitionDataAssociation> outDAs = dataAssociationGroup.getOutDataAssociations();
		for(String resultName : outDAs.keySet()) {
			HAPExecutableDataAssociation outExe = HAPProcessorDataAssociation.processDataAssociation(
					outDAs.get(resultName),
					new HAPContextStructureReferenceValueStructure(parentComplexEntityExe.getValueContext(), null, parentContext.getCurrentValueStructureDomain()),
					new HAPContextStructureReferenceInteractiveResult(results.get(resultName)),
					parentContext.getRuntimeEnvironment());
			out.addOutDataAssociation(resultName, outExe);
		}
		
		return out;
	}
}
