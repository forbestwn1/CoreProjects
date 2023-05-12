package com.nosliw.data.core.component.event;

import com.nosliw.data.core.domain.common.interactive.HAPUtilityInteractive;
import com.nosliw.data.core.domain.entity.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.domain.entity.dataassociation.mapping.HAPExecutableDataAssociationMapping;
import com.nosliw.data.core.handler.HAPExecutableHandler;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPProcessEvent {

	public static HAPExecutableEvent processEventDefinition(HAPDefinitionEvent eventDef, HAPValueStructure parentStructure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableEvent out = new HAPExecutableEvent();
		
		eventDef.cloneToEntityInfo(out);

		HAPContainerStructure parentStructures = HAPContainerStructure.createDefault(parentStructure);
		
		HAPDefinitionEvent solidateEventDef = eventDef.cloneEventDefinition();
		HAPUtilityInteractive.solidateInteractiveResult(solidateEventDef, parentStructures, null, true, null);
		
		HAPValueStructureDefinitionFlat valueStructure = HAPUtilityInteractive.buildValueStructureForResule(solidateEventDef);
		
		HAPDefinitionDataAssociationMapping dataAssociation = HAPUtilityInteractive.buildDataAssociationForResult(solidateEventDef);

		HAPExecutableDataAssociationMapping exeDataAssociation = (HAPExecutableDataAssociationMapping)HAPProcessorDataAssociation.processDataAssociation(parentStructures, dataAssociation, HAPContainerStructure.createDefault(valueStructure), null, runtimeEnv);

		out.setDataAssociation(exeDataAssociation);
		out.setValueStructure(valueStructure);
		
		
		return out;
	}
	
	public static HAPExecutableHandlerEvent processEventHandler(HAPDefinitionHandlerEvent eventHandlerDef, HAPExecutableEvent event, HAPValueStructure parentStructure, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableHandlerEvent out = new HAPExecutableHandlerEvent();
		out.setEventName(eventHandlerDef.getEventName());
		out.setHandler(new HAPExecutableHandler(eventHandlerDef.getHandler()));
		
		out.setInDataAssociation(HAPProcessorDataAssociation.processDataAssociation(
				HAPContainerStructure.createDefault(event.getValueStructure()), 
				eventHandlerDef.getDataAssociation(), 
				HAPContainerStructure.createDefault(parentStructure), 
				null, runtimeEnv));
		return out;
	}
}
