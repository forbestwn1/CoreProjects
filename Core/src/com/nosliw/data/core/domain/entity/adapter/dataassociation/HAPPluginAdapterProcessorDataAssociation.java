package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginAdapterProcessor;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.mapping.HAPExecutableDataAssociationMapping;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.none.HAPDefinitionDataAssociationNone;
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
		
		
		String type = dataAssociation.getType();
		String direction = dataAssociation.getDirection();
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
			HAPDefinitionDataAssociationMapping valueMappingDA = (HAPDefinitionDataAssociationMapping)dataAssociation;
			HAPExecutableDataAssociationMapping daMappingExe = new  HAPExecutableDataAssociationMapping(valueMappingDA, null, null);
			if(direction.equals(HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM)) {
				HAPProcessorDataAssociationMapping.processValueMapping(
						daMappingExe,
						parentComplexEntityExe.getValueContext(), 
						parentContext.getCurrentValueStructureDomain(), 
						valueMappingDA, 
						childComplexEntityExe.getValueContext(), 
						childContext.getCurrentValueStructureDomain(), 
						parentContext.getRuntimeEnvironment());
			}
			else {
				HAPProcessorDataAssociationMapping.processValueMapping(
						daMappingExe,
						childComplexEntityExe.getValueContext(), 
						childContext.getCurrentValueStructureDomain(), 
						valueMappingDA, 
						parentComplexEntityExe.getValueContext(), 
						parentContext.getCurrentValueStructureDomain(), 
						parentContext.getRuntimeEnvironment());
			}
			out = daMappingExe;
//			return HAPProcessorDataAssociationMapping.processDataAssociation(input, (HAPDefinitionDataAssociationMapping)dataAssociation, output, configure, runtimeEnv);
		
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
//			HAPDefinitionDataAssociationMapping mappingDataAssociation = HAPProcessorDataAssociationMirror.convertToDataAssociationMapping(input, (HAPDefinitionDataAssociationMirror)dataAssociation, output);
//			return processDataAssociation(input, mappingDataAssociation, output, configure, runtimeEnv);

		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
//			return HAPProcessorDataAssociationNone.processDataAssociation(input, (HAPDefinitionDataAssociationNone)dataAssociation, output, configure, runtimeEnv);
		}
		dataAssociation.cloneToEntityInfo(out);
		return out;
	}

	
}
