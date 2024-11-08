package com.nosliw.core.application.common.task;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafValue;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPUtilityTaskTrigguerValuePort {

	public static Pair<HAPGroupValuePorts, HAPGroupValuePorts> createValuePortGroupForEventHandle(HAPInfoTrigguerTask taskTrigguerInfo, HAPDomainValueStructure valueStructureDomain) {
		
		HAPGroupValuePorts internalValurPortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_EVENTDATA); 
		HAPGroupValuePorts externalValurPortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_EVENTDATA); 
		
		HAPValuePort internalValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_EVENT, HAPConstantShared.IO_DIRECTION_OUT);
		internalValuePort.setName(HAPConstantShared.VALUEPORT_NAME_EVENT);
		HAPValuePort externalValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_EVENT, HAPConstantShared.IO_DIRECTION_IN); 
		externalValuePort.setName(HAPConstantShared.VALUEPORT_NAME_EVENT);
		
		Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>();
		HAPRootInStructure root = new HAPRootInStructure();
		root.setDefinition(taskTrigguerInfo.getEventDataDefinition());
		root.setName(HAPConstantShared.NAME_ROOT_EVENT);
		roots.add(root);
		
		String valueStructureId = valueStructureDomain.newValueStructure(roots, null, null, null);
		internalValuePort.addValueStructureId(valueStructureId);
		externalValuePort.addValueStructureId(valueStructureId);
		
		internalValurPortGroup.addValuePort(internalValuePort);
		externalValurPortGroup.addValuePort(externalValuePort);
		
		return Pair.of(internalValurPortGroup, externalValurPortGroup);
	}
	
	public static Pair<HAPGroupValuePorts, HAPGroupValuePorts> createValuePortGroupForDataValidation(HAPInfoTrigguerTask taskTrigguerInfo, HAPDomainValueStructure valueStructureDomain) {
		
		HAPGroupValuePorts internalValurPortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_VALIDATIONDATA); 
		HAPGroupValuePorts externalValurPortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_VALIDATIONDATA); 
		
		//value port for data
		{
			HAPValuePort internalDataValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_VALIDATIONDATA, HAPConstantShared.IO_DIRECTION_OUT);
			internalDataValuePort.setName(HAPConstantShared.VALUEPORT_NAME_VALIDATIONDATA);
			HAPValuePort externalDataValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_VALIDATIONDATA, HAPConstantShared.IO_DIRECTION_IN); 
			externalDataValuePort.setName(HAPConstantShared.VALUEPORT_NAME_VALIDATIONDATA);
			Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>();
			HAPRootInStructure root = new HAPRootInStructure();
			root.setDefinition(taskTrigguerInfo.getEventDataDefinition());
			root.setName(HAPConstantShared.NAME_ROOT_DATA);
			roots.add(root);
			
			String valueStructureId = valueStructureDomain.newValueStructure(roots, null, null, null);
			internalDataValuePort.addValueStructureId(valueStructureId);
			externalDataValuePort.addValueStructureId(valueStructureId);
			internalValurPortGroup.addValuePort(internalDataValuePort);
			externalValurPortGroup.addValuePort(externalDataValuePort);
		}

		//value port for result
		{
			HAPValuePort internalErrorResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_ERROR, HAPConstantShared.IO_DIRECTION_IN); 
			internalErrorResultValuePort.setName(HAPConstantShared.VALUEPORT_NAME_ERROR);
			HAPValuePort externalErrorResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_ERROR, HAPConstantShared.IO_DIRECTION_OUT); 
			externalErrorResultValuePort.setName(HAPConstantShared.VALUEPORT_NAME_ERROR);
			
			Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>();
			HAPRootInStructure root = new HAPRootInStructure();
			root.setDefinition(new HAPElementStructureLeafValue());
			root.setName(HAPConstantShared.NAME_ROOT_ERROR);
			roots.add(root);
			
			String valueStructureId = valueStructureDomain.newValueStructure(roots, null, null, null);
			internalErrorResultValuePort.addValueStructureId(valueStructureId);
			externalErrorResultValuePort.addValueStructureId(valueStructureId);
			
			internalValurPortGroup.addValuePort(internalErrorResultValuePort);
			externalValurPortGroup.addValuePort(externalErrorResultValuePort);
		}
		
		return Pair.of(internalValurPortGroup, externalValurPortGroup);
	}
	
}

