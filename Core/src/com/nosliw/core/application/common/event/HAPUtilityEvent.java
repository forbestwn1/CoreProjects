package com.nosliw.core.application.common.event;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPUtilityEvent {

	public static Pair<HAPGroupValuePorts, HAPGroupValuePorts> createValuePortGroupForEvent(HAPInfoEvent eventInfo, HAPDomainValueStructure valueStructureDomain) {
		
		HAPGroupValuePorts internalValurPortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_EVENT); 
		HAPGroupValuePorts externalValurPortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_EVENT); 
		
		HAPValuePort internalValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_EVENT, HAPConstantShared.IO_DIRECTION_OUT);
		internalValuePort.setName(HAPConstantShared.VALUEPORT_NAME_EVENT);
		HAPValuePort externalValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_EVENT, HAPConstantShared.IO_DIRECTION_IN); 
		externalValuePort.setName(HAPConstantShared.VALUEPORT_NAME_EVENT);
		
		Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>();
		HAPRootInStructure root = new HAPRootInStructure();
		root.setDefinition(eventInfo.getEventDataDefinition());
		root.setName(HAPConstantShared.NAME_ROOT_EVENT);
		roots.add(root);
		
		String valueStructureId = valueStructureDomain.newValueStructure(roots, null, null, null);
		internalValuePort.addValueStructureId(valueStructureId);
		externalValuePort.addValueStructureId(valueStructureId);
		
		internalValurPortGroup.addValuePort(internalValuePort);
		externalValurPortGroup.addValuePort(externalValuePort);
		
		return Pair.of(internalValurPortGroup, externalValurPortGroup);
	}
	
}
