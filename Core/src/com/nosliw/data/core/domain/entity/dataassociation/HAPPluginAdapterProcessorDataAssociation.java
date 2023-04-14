package com.nosliw.data.core.domain.entity.dataassociation;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginAdapterProcessor;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPDefinitionValueMapping;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPExecutableDataAssociationMapping;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPExecutableValueMapping;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.domain.entity.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginAdapterProcessorDataAssociation implements HAPPluginAdapterProcessor{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPPluginAdapterProcessorDataAssociation(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public String getAdapterType() {  return HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION;  }

	@Override
	public Object process(Object adpaterObj, HAPExecutableImp childEntityExecutable, HAPContextProcessor childContext, HAPExecutableImp parentEntityExecutable, HAPContextProcessor parentContext) {
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
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
			HAPDefinitionDataAssociationMapping valueMappingDA = (HAPDefinitionDataAssociationMapping)dataAssociation;
			Map<String, HAPDefinitionValueMapping> valueMappings = valueMappingDA.getMappings();
			HAPExecutableDataAssociationMapping daMappingExe = new  HAPExecutableDataAssociationMapping(valueMappingDA, null, null);
			for(String targetName : valueMappings.keySet()) {
				HAPExecutableValueMapping valueMappingExe = HAPProcessorDataAssociationMapping.processValueMapping(
						parentComplexEntityExe.getValueContext(), 
						parentContext.getCurrentValueStructureDomain(), 
						valueMappings.get(targetName), 
						childComplexEntityExe.getValueContext(), 
						childContext.getCurrentValueStructureDomain(), 
						this.m_runtimeEnv);
				
				daMappingExe.addMapping(targetName, valueMappingExe);
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
