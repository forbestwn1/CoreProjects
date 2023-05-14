package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginAdapterProcessor;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPPluginAdapterProcessorDataAssociation implements HAPPluginAdapterProcessor{

	@Override
	public String getAdapterType() {  return HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION;  }

	@Override
	public Object process(Object adpaterObj, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext) {
		HAPExecutableDataAssociation out = null;
		
		HAPDefinitionDataAssociation dataAssociation = null;
		if(adpaterObj==null)   dataAssociation = new HAPDefinitionDataAssociationNone();
		else {
			HAPDefinitionEntityDataAssciation daEntityDef = HAPUtilityDomain.getEntity(adpaterObj, parentContext, HAPDefinitionEntityDataAssciation.class);
			dataAssociation = daEntityDef.getDataAssociation();
		}
		
		HAPExecutableEntityComplex parentComplexEntityExe = (HAPExecutableEntityComplex)parentEntityExecutable;
		HAPExecutableEntityComplex childComplexEntityExe = (HAPExecutableEntityComplex)childEntityExecutable;
		
		out = HAPProcessorDataAssociation.processDataAssociation(
				dataAssociation,
				new HAPContextStructureReferenceValueStructure(parentComplexEntityExe.getValueContext(), null, parentContext.getCurrentValueStructureDomain()),
				new HAPContextStructureReferenceValueStructure(childComplexEntityExe.getValueContext(), null, childContext.getCurrentValueStructureDomain()),
				parentContext.getRuntimeEnvironment());

		return out;
	}
}
