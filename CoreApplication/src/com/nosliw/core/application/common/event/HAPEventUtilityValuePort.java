package com.nosliw.core.application.common.event;

import java.util.HashSet;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.valueport.HAPValuePort;

public class HAPEventUtilityValuePort {
	
	public static  void buildValuePortGroupForEventHandler(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair, HAPEventDefinition eventDef, HAPDomainValueStructure valueStructureDomain) {
		String valueStructureId = valueStructureDomain.newValueStructure(new HashSet(eventDef.getDataDefinition().getRoots().values()), null, null, eventDef.getName());
		
		Pair<HAPValuePort, HAPValuePort> eventValuePortPair =  HAPUtilityValuePort.getOrCreateValuePort(
				valuePortContainerPair, 
				HAPConstantShared.VALUEPORTGROUP_TYPE_EVENT, 
				HAPConstantShared.VALUEPORT_TYPE_EVENT, 
				HAPConstantShared.VALUEPORT_TYPE_EVENT,
				Pair.of(HAPConstantShared.IO_DIRECTION_OUT, HAPConstantShared.IO_DIRECTION_IN));
		
		eventValuePortPair.getLeft().addValueStructureId(valueStructureId);
		eventValuePortPair.getRight().addValueStructureId(valueStructureId);
		
	}
	
}
