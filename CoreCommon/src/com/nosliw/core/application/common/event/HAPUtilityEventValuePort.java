package com.nosliw.core.application.common.event;

import java.util.HashSet;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractiveTaskValuePort;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.valueport.HAPValuePort;

public class HAPUtilityEventValuePort {

	public static void buildValuePortGroupForInteractiveTaskEventHandler(HAPBundle bundle, HAPPath idPath, String rootNameIfNotProvide, HAPElementStructure dataElement, HAPDomainValueStructure valueStructureDomain) {
		idPath = figureoutTaskPath(bundle, idPath, rootNameIfNotProvide);
		HAPManualBrick childBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle, idPath, rootNameIfNotProvide);
		if(childBrick!=null) {
			HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveTaskEventHandler(Pair.of(childBrick.getOtherInternalValuePortContainer(), childBrick.getOtherExternalValuePortContainer()), dataElement, valueStructureDomain);
		}
	}
	
	public static void buildValuePortGroupForEvent(HAPContainerValuePorts valuePortContainer, HAPWithEvents withEvent, HAPDomainValueStructure valueStructureDomain) {

		
		
		
		for(HAPEventDefinition eventDef : withEvent.getEvents()) {
			HAPValuePort valuePort = HAPUtilityValuePort.getOrCreateValuePort(valuePortContainer, HAPConstantShared.VALUEPORTGROUP_TYPE_EVENT, HAPConstantShared.VALUEPORT_TYPE_EVENT, HAPConstantShared.VALUEPORT_TYPE_EVENT, HAPConstantShared.IO_DIRECTION_OUT);
			HAPInfo valueStructureInfo = new HAPInfoImpSimple();
			valueStructureInfo.setValue(HAPConstantShared.UIRESOURCE_CONTEXTINFO_INSTANTIATE, HAPConstantShared.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL);
			String valueStructureId = valueStructureDomain.newValueStructure(new HashSet(eventDef.getDataDefinition().getRoots().values()), null, valueStructureInfo, eventDef.getName());
			valuePort.addValueStructureId(valueStructureId);
		}
	}

}
