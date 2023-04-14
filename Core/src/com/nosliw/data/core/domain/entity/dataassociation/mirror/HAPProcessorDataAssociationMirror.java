package com.nosliw.data.core.domain.entity.dataassociation.mirror;

import java.util.List;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPDefinitionValueMapping;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPReferenceRootInStrucutre;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPProcessorDataAssociationMirror {

	
	public static HAPDefinitionDataAssociationMapping convertToDataAssociationMapping(HAPContainerStructure input, HAPDefinitionDataAssociationMirror dataAssociation, HAPContainerStructure output) {
		HAPDefinitionDataAssociationMapping mappigDataAssociation = new HAPDefinitionDataAssociationMapping();
		
		for(String inputStructureName : input.getStructureNames()) {
			HAPValueStructure inputStructure = input.getStructure(inputStructureName);
			HAPValueStructure outputStructure = output.getStructure(inputStructureName);
			if(outputStructure!=null) {
				if(HAPUtilityBasic.isEquals(inputStructure.getStructureType(), outputStructure.getStructureType())) {
					
					HAPDefinitionValueMapping valueMapping = new HAPDefinitionValueMapping();
					
					for(HAPRootStructure inRoot : inputStructure.getAllRoots()) {
						HAPReferenceRootInStrucutre rootRef = inputStructure.getRootReferenceById(inRoot.getLocalId());
						List<HAPRootStructure> outputRoots = outputStructure.resolveRoot(rootRef, false);
						if(outputRoots!=null&&outputRoots.size()>0) {
							String rootName = rootRef.toStringValue(HAPSerializationFormat.LITERATE);
							HAPRootStructure mappingRoot = HAPUtilityStructure.createRootWithRelativeElement(rootName, inputStructureName);
							valueMapping.addItem(rootName, mappingRoot);
						}
					}
					
					if(!valueMapping.isEmpty()) {
						mappigDataAssociation.addAssociation(inputStructureName, valueMapping);
					}
				}
			}
		}
		return mappigDataAssociation;
	}
	
}
