package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginAdapterProcessorImp;
import com.nosliw.data.core.domain.valuecontext.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPPluginAdapterProcessorDataAssociation extends HAPPluginAdapterProcessorImp{

	public HAPPluginAdapterProcessorDataAssociation() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION, HAPExecutableEntityDataAssciation.class);
	}

	@Override
	public void postProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {
		HAPDefinitionDataAssociation dataAssociation = null;
		HAPDefinitionEntityDataAssciation daEntityDef = HAPUtilityDomain.getEntity(adapterExe.getDefinitionEntityId(), parentContext, HAPDefinitionEntityDataAssciation.class);
		dataAssociation = daEntityDef.getDataAssociation();
		
		HAPExecutableEntityComplex parentComplexEntityExe = (HAPExecutableEntityComplex)parentEntityExecutable;
		HAPExecutableEntityComplex childComplexEntityExe = (HAPExecutableEntityComplex)childEntityExecutable;
		
		HAPExecutableDataAssociation dataAssociationExe = HAPProcessorDataAssociation.processDataAssociation(
				dataAssociation,
				new HAPContextStructureReferenceValueStructure(parentComplexEntityExe.getValueContext(), null, parentContext.getCurrentValueStructureDomain()),
				new HAPContextStructureReferenceValueStructure(childComplexEntityExe.getValueContext(), null, childContext.getCurrentValueStructureDomain()),
				parentContext.getRuntimeEnvironment());

		((HAPExecutableEntityDataAssciation)adapterExe).setDataAssciation(dataAssociationExe);
	}
}
