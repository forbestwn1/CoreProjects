package com.nosliw.data.core.component.event;

import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPExecutableDataAssociationMapping;
import com.nosliw.data.core.interactive.HAPUtilityInteractive;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPProcessEvent {

	public static HAPExecutableEvent process(HAPDefinitionEvent eventDef, HAPContainerStructure parentStructures, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableEvent out = new HAPExecutableEvent();
		
		eventDef.cloneToEntityInfo(out);

		HAPDefinitionEvent solidateEventDef = eventDef.cloneEventDefinition();
		HAPUtilityInteractive.solidateInteractiveResult(solidateEventDef, parentStructures, null, true, null);
		
		HAPValueStructureDefinitionFlat valueStructure = HAPUtilityInteractive.buildValueStructureForResule(solidateEventDef);
		
		HAPDefinitionDataAssociationMapping dataAssociation = HAPUtilityInteractive.buildDataAssociationForResult(solidateEventDef);

		HAPExecutableDataAssociationMapping exeDataAssociation = (HAPExecutableDataAssociationMapping)HAPProcessorDataAssociation.processDataAssociation(parentStructures, dataAssociation, HAPContainerStructure.createDefault(valueStructure), null, runtimeEnv);

		out.setDataAssociation(exeDataAssociation);
		out.setValueStructure(valueStructure);
		
		
		return out;
	}
}
