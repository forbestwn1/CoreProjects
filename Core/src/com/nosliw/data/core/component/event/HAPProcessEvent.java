package com.nosliw.data.core.component.event;

import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPExecutableDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPProcessEvent {

	public static HAPExecutableEvent process(HAPDefinitionEvent eventDef, HAPValueStructure parentStructure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionFlat outputValueStructure = new HAPValueStructureDefinitionFlat();
		
		HAPDefinitionDataAssociationMapping dataAssociatioin = new HAPDefinitionDataAssociationMapping();
		dataAssociatioin.addAssociation(null, eventDef.getValueMapping());
		
		HAPProcessorDataAssociationMapping.enhanceDataAssociationEndPointContext(HAPContainerStructure.createDefault(parentStructure), false, dataAssociatioin, HAPContainerStructure.createDefault(outputValueStructure), true, runtimeEnv);
		
//		HAPProcessorDataAssociationMapping.enhanceAssociationEndPointContext(HAPContainerStructure.createDefault(parentStructure), false, eventDef.getValueMapping(), outputValueStructure, true, runtimeEnv);
		
		HAPExecutableDataAssociationMapping dataAssociationExe = HAPProcessorDataAssociationMapping.processDataAssociation(
				HAPContainerStructure.createDefault(parentStructure), 
				dataAssociatioin, 
				HAPContainerStructure.createDefault(outputValueStructure), 
				null, 
				runtimeEnv);
		
//		HAPExecutableMapping valueMappingExe = HAPProcessorDataAssociationMapping.processValueMapping(HAPContainerStructure.createDefault(parentStructure), eventDef.getValueMapping(), outputValueStructure, null, null, runtimeEnv);
		
		HAPExecutableEvent out = new HAPExecutableEvent(outputValueStructure, dataAssociationExe);
		eventDef.cloneToEntityInfo(out);
		
		return out;
	}
	
	
}
