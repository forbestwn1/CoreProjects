package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPAdapterDataAssciation;
import com.nosliw.core.application.division.manual.HAPPluginProcessorAdapterImp;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPManaualPluginAdapterProcessorDataAssociation extends HAPPluginProcessorAdapterImp{

	public HAPManaualPluginAdapterProcessorDataAssociation() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION);
	}

	@Override
	public void postProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {
		HAPDefinitionDataAssociation dataAssociation = null;
		HAPManualAdapterDataAssciation daEntityDef = HAPUtilityDomain.getEntity(adapterExe.getDefinitionEntityId(), parentContext, HAPManualAdapterDataAssciation.class);
		dataAssociation = daEntityDef.getDataAssociation();
		
		HAPExecutableEntityComplex parentComplexEntityExe = (HAPExecutableEntityComplex)parentEntityExecutable;
		HAPExecutableEntityComplex childComplexEntityExe = (HAPExecutableEntityComplex)childEntityExecutable;
		
		HAPExecutableDataAssociation dataAssociationExe = HAPProcessorDataAssociation.processDataAssociation(
				dataAssociation,
				parentEntityExecutable,
				parentContext,
				(HAPExecutableEntity)childEntityExecutable,
				childContext,
				parentContext.getRuntimeEnvironment());

		((HAPAdapterDataAssciation)adapterExe).setDataAssciation(dataAssociationExe);
	}
}