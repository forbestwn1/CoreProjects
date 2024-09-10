package com.nosliw.core.application.common.interactive;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.data.HAPData;

public class HAPUtilityInteractiveValuePort {

	public static Pair<HAPGroupValuePorts, HAPGroupValuePorts> buildValuePortGroupForInteractiveExpression(HAPInteractiveExpression expressionInteractive, HAPDomainValueStructure valueStructureDomain) {
		HAPGroupValuePorts internalValuePortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION);
		HAPGroupValuePorts externalValuePortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION);

		//request value structure
		Pair<HAPValuePort, HAPValuePort> requestValuePortPair = createInteractiveRequestValuePort(expressionInteractive.getRequestParms(), valueStructureDomain); 
		internalValuePortGroup.addValuePort(requestValuePortPair.getLeft());
		externalValuePortGroup.addValuePort(requestValuePortPair.getRight());

		
		Pair<HAPValuePort, HAPValuePort> resultValuePortPair = createExpressionInteractiveResultValuePort(expressionInteractive.getResult(), valueStructureDomain);
		internalValuePortGroup.addValuePort(resultValuePortPair.getLeft());
		externalValuePortGroup.addValuePort(resultValuePortPair.getRight());
		
		return Pair.of(internalValuePortGroup, externalValuePortGroup);
	}
	

	public static Pair<HAPGroupValuePorts, HAPGroupValuePorts> buildValuePortGroupForInteractiveTask(HAPInteractiveTask taskInteractive, HAPDomainValueStructure valueStructureDomain) {
		HAPGroupValuePorts internalValuePortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVETASK);
		HAPGroupValuePorts externalValuePortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVETASK);

		//request value port
		Pair<HAPValuePort, HAPValuePort> requestValuePortPair = createInteractiveRequestValuePort(taskInteractive.getRequestParms(), valueStructureDomain); 
		internalValuePortGroup.addValuePort(requestValuePortPair.getLeft());
		externalValuePortGroup.addValuePort(requestValuePortPair.getRight());

		//result value port
		for(HAPInteractiveResultTask result : taskInteractive.getResult()) {
			Pair<HAPValuePort, HAPValuePort> resultValuePortPair = createTaskInteractiveResultValuePort(result, valueStructureDomain);
			internalValuePortGroup.addValuePort(resultValuePortPair.getLeft());
			externalValuePortGroup.addValuePort(resultValuePortPair.getRight());
		}
		return Pair.of(internalValuePortGroup, externalValuePortGroup);
	}

	private static Pair<HAPValuePort, HAPValuePort> createTaskInteractiveResultValuePort(HAPInteractiveResultTask interactiveResult, HAPDomainValueStructure valueStructureDomain) {
		Set<HAPRootInStructure> outputRoots = new HashSet<HAPRootInStructure>();
		for(HAPResultElementInInteractiveTask element : interactiveResult.getOutput()) {
			HAPRootInStructure root = new HAPRootInStructure(new HAPElementStructureLeafData(element.getCriteria()), element);
			outputRoots.add(root);
		}
		String resultVSId = valueStructureDomain.newValueStructure(outputRoots, null, null, null);
		
		HAPValuePort internalResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, HAPConstantShared.IO_DIRECTION_IN);
		internalResultValuePort.setName(buildResultValuePortName(interactiveResult.getName()));
		internalResultValuePort.addValueStructureId(resultVSId);

		HAPValuePort externalResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, HAPConstantShared.IO_DIRECTION_OUT);
		externalResultValuePort.setName(buildResultValuePortName(interactiveResult.getName()));
		externalResultValuePort.addValueStructureId(resultVSId);

		return Pair.of(internalResultValuePort, externalResultValuePort);
}

	private static String buildResultValuePortName(String resultName) {
		return HAPUtilityNamingConversion.cascadeComponents(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT, resultName, HAPConstantShared.SEPERATOR_PREFIX);
	}

	
	private static Pair<HAPValuePort, HAPValuePort> createExpressionInteractiveResultValuePort(HAPInteractiveResultExpression interactiveResult, HAPDomainValueStructure valueStructureDomain) {
		//result value structure
		Set<HAPRootInStructure> resultRoots = new HashSet<HAPRootInStructure>();
		HAPRootInStructure resultRoot = new HAPRootInStructure(new HAPElementStructureLeafData(interactiveResult.getDataCriteria()), null);
		resultRoot.setName(HAPConstantShared.NAME_ROOT_RESULT);
		resultRoots.add(resultRoot);
		String resultVSId = valueStructureDomain.newValueStructure(resultRoots, null, null, null);
		
		HAPValuePort internalResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, HAPConstantShared.IO_DIRECTION_IN);
		internalResultValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT);
		internalResultValuePort.addValueStructureId(resultVSId);
		
		HAPValuePort externalResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, HAPConstantShared.IO_DIRECTION_OUT);
		externalResultValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT);
		externalResultValuePort.addValueStructureId(resultVSId);
		
		return Pair.of(internalResultValuePort, externalResultValuePort);
	}
	
	private static Pair<HAPValuePort, HAPValuePort> createInteractiveRequestValuePort(List<HAPRequestParmInInteractive> requestParms, HAPDomainValueStructure valueStructureDomain) {
		Set<HAPRootInStructure> requestRoots = new HashSet<HAPRootInStructure>();
		Map<String, HAPData> defaultValue = new LinkedHashMap<String, HAPData>(); 
		for(HAPRequestParmInInteractive parm : requestParms) {
			HAPRootInStructure root = new HAPRootInStructure(new HAPElementStructureLeafData(parm.getCriteria()), parm);
			requestRoots.add(root);
			defaultValue.put(parm.getName(), parm.getDefaultValue());
		}
		String requestVSId = valueStructureDomain.newValueStructure(requestRoots, defaultValue, null, null);
		
		//request value port -- internal
		HAPValuePort internalReqeustValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST, HAPConstantShared.IO_DIRECTION_OUT);
		internalReqeustValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		internalReqeustValuePort.addValueStructureId(requestVSId);
		
		//request value port -- external
		HAPValuePort externalReqeustValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST, HAPConstantShared.IO_DIRECTION_IN);
		externalReqeustValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		externalReqeustValuePort.addValueStructureId(requestVSId);
		
		return Pair.of(internalReqeustValuePort, externalReqeustValuePort);
	}
	
}
