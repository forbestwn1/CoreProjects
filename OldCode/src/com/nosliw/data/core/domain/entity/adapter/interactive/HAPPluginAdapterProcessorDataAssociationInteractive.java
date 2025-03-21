package com.nosliw.data.core.domain.entity.adapter.interactive;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPDefinitionInteractive;
import com.nosliw.core.application.common.interactive.HAPInteractiveResultTask;
import com.nosliw.core.application.common.interactive1.HAPBrickInteractive;
import com.nosliw.core.application.common.interactive1.HAPContextStructureReferenceInteractiveRequest;
import com.nosliw.core.application.common.interactive1.HAPContextStructureReferenceInteractiveResult;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableGroupDataAssociationForTask;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginAdapterProcessorImp;
import com.nosliw.data.core.domain.valuecontext.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPPluginAdapterProcessorDataAssociationInteractive extends HAPPluginAdapterProcessorImp{

	public HAPPluginAdapterProcessorDataAssociationInteractive() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONINTERACTIVE, HAPExecutableEntityDataAssociationInteractive.class);
	}

	@Override
	public void postProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {
		HAPExecutableGroupDataAssociationForTask dataAssociationGroupExe = new HAPExecutableGroupDataAssociationForTask();
		
		HAPDefinitionEntityDataAssociationInteractive adapterDef = HAPUtilityDomain.getEntity(adapterExe.getDefinitionEntityId(), parentContext, HAPDefinitionEntityDataAssociationInteractive.class);
		HAPDefinitionGroupDataAssociationForTask dataAssociationGroup = adapterDef.getDataAssociation();
		
		HAPExecutableEntityComplex parentComplexEntityExe = (HAPExecutableEntityComplex)parentEntityExecutable;
		
		HAPBrickInteractive interactiveEntityExe = (HAPBrickInteractive)childEntityExecutable;
		HAPDefinitionInteractive interactiveExe = interactiveEntityExe.getInteractive();
		
		//data association for request parms
		HAPExecutableDataAssociation inExe = HAPProcessorDataAssociation.processDataAssociation(
				dataAssociationGroup.getInDataAssociation(),
				new HAPContextStructureReferenceValueStructure(parentComplexEntityExe.getValueContext(), null, parentContext.getCurrentValueStructureDomain()),
				new HAPContextStructureReferenceInteractiveRequest(interactiveExe.getRequestParms()),
				parentContext.getRuntimeEnvironment());
		dataAssociationGroupExe.setInDataAssociation(inExe);

		//data association for result
		Map<String, HAPInteractiveResultTask> results = interactiveExe.getResults();
		Map<String, HAPDefinitionDataAssociation> outDAs = dataAssociationGroup.getOutDataAssociations();
		for(String resultName : outDAs.keySet()) {
			HAPExecutableDataAssociation outExe = HAPProcessorDataAssociation.processDataAssociation(
					outDAs.get(resultName),
					new HAPContextStructureReferenceValueStructure(parentComplexEntityExe.getValueContext(), null, parentContext.getCurrentValueStructureDomain()),
					new HAPContextStructureReferenceInteractiveResult(results.get(resultName)),
					parentContext.getRuntimeEnvironment());
			dataAssociationGroupExe.addOutDataAssociation(resultName, outExe);
		}
		((HAPExecutableEntityDataAssociationInteractive)adapterExe).setDataAssciation(dataAssociationGroupExe);
	}
}
