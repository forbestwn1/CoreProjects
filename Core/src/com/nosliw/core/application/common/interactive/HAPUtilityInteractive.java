package com.nosliw.core.application.common.interactive;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.data.HAPData;

public class HAPUtilityInteractive {

	public static Pair<HAPGroupValuePorts, HAPGroupValuePorts> buildValuePortGroupForInteractiveExpression(HAPInteractiveExpression expressionInteractive, HAPDomainValueStructure valueStructureDomain) {
		HAPGroupValuePorts internalValuePortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION);
		HAPGroupValuePorts externalValuePortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION);

		//request value structure
		Set<HAPRootInStructure> requestRoots = new HashSet<HAPRootInStructure>();
		Map<String, HAPData> defaultValue = new LinkedHashMap<String, HAPData>(); 
		List<HAPRequestParmInInteractive> requestParms = expressionInteractive.getRequestParms();
		for(HAPRequestParmInInteractive parm : requestParms) {
			HAPRootInStructure root = new HAPRootInStructure(new HAPElementStructureLeafData(parm.getCriteria()), parm);
			requestRoots.add(root);
			defaultValue.put(parm.getName(), parm.getDefaultValue());
		}
		String requestVSId = valueStructureDomain.newValueStructure(requestRoots, defaultValue, null, null);

		//result value structure
		Set<HAPRootInStructure> resultRoots = new HashSet<HAPRootInStructure>();
		HAPRootInStructure resultRoot = new HAPRootInStructure(new HAPElementStructureLeafData(expressionInteractive.getResult().getDataCriteria()), null);
		resultRoot.setName(HAPConstantShared.NAME_ROOT_RESULT);
		resultRoots.add(resultRoot);
		String resultVSId = valueStructureDomain.newValueStructure(resultRoots, null, null, null);

		
		//request value port -- internal
		HAPValuePort internalReqeustValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST, HAPConstantShared.IO_DIRECTION_BOTH);
		internalReqeustValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		internalReqeustValuePort.addValueStructureId(requestVSId);
		internalValuePortGroup.addValuePort(internalReqeustValuePort, true);
		
		//request value port -- external
		HAPValuePort externalReqeustValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST, HAPConstantShared.IO_DIRECTION_IN);
		externalReqeustValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		externalReqeustValuePort.addValueStructureId(requestVSId);
		externalValuePortGroup.addValuePort(externalReqeustValuePort, true);
		
		HAPValuePort internalResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, HAPConstantShared.IO_DIRECTION_IN);
		internalResultValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT);
		internalResultValuePort.addValueStructureId(resultVSId);
		internalValuePortGroup.addValuePort(internalResultValuePort, true);
		
		HAPValuePort externalResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, HAPConstantShared.IO_DIRECTION_BOTH);
		externalResultValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT);
		externalResultValuePort.addValueStructureId(resultVSId);
		externalValuePortGroup.addValuePort(externalResultValuePort, true);
		
		return Pair.of(internalValuePortGroup, externalValuePortGroup);
	}
	
	
}
