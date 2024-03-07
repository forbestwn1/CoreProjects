package com.nosliw.data.core.domain.entity.adapter.task;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableGroupDataAssociationForTask;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginAdapterProcessorImp;
import com.nosliw.data.core.domain.valuecontext.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.interactive.HAPContextStructureReferenceInteractiveRequest;
import com.nosliw.data.core.interactive.HAPContextStructureReferenceInteractiveResult;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;
import com.nosliw.data.core.interactive.HAPDefinitionInteractiveResult;
import com.nosliw.data.core.interactive.HAPExecutableEntityInteractive;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPPluginAdapterProcessorDataAssociationTask extends HAPPluginAdapterProcessorImp{

	public HAPPluginAdapterProcessorDataAssociationTask() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONTASK, HAPExecutableEntityDataAssociationTask.class);
	}

	@Override
	public void postProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {
		HAPExecutableGroupDataAssociationForTask dataAssociationGroupExe = new HAPExecutableGroupDataAssociationForTask();
		
		HAPDefinitionEntityDataAssociationTask adapterDef = HAPUtilityDomain.getEntity(adapterExe.getDefinitionEntityId(), parentContext, HAPDefinitionEntityDataAssociationTask.class);
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
		dataAssociationGroupExe.setInDataAssociation(inExe);

		//data association for result
		Map<String, HAPDefinitionInteractiveResult> results = interactiveExe.getResults();
		Map<String, HAPDefinitionDataAssociation> outDAs = dataAssociationGroup.getOutDataAssociations();
		for(String resultName : outDAs.keySet()) {
			HAPExecutableDataAssociation outExe = HAPProcessorDataAssociation.processDataAssociation(
					outDAs.get(resultName),
					new HAPContextStructureReferenceValueStructure(parentComplexEntityExe.getValueContext(), null, parentContext.getCurrentValueStructureDomain()),
					new HAPContextStructureReferenceInteractiveResult(results.get(resultName)),
					parentContext.getRuntimeEnvironment());
			dataAssociationGroupExe.addOutDataAssociation(resultName, outExe);
		}
		((HAPExecutableEntityDataAssociationTask)adapterExe).setDataAssciation(dataAssociationGroupExe);
	}
}
