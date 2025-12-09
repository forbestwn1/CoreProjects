package com.nosliw.core.application.valueport;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.HAPUtilityBrickPath;
import com.nosliw.core.application.HAPUtilityBrickReference;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPUtilityValuePort {

	public static Pair<HAPValuePort, HAPValuePort> getOrCreateValuePort(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainers, String valuePortGroupType, String valuePortType, String valuePortName, Pair<String, String> io){
		
		return Pair.of(
				valuePortContainers.getLeft()==null? null : getOrCreateValuePort(valuePortContainers.getLeft(), valuePortGroupType, valuePortType, valuePortName, io.getLeft()),
				valuePortContainers.getRight()==null? null : getOrCreateValuePort(valuePortContainers.getRight(), valuePortGroupType, valuePortType, valuePortName, io.getRight()));
	}
	
	public static HAPValuePort getOrCreateValuePort(HAPContainerValuePorts valuePortContainer, String valuePortGroupType, String valuePortType, String valuePortName, String io){
		HAPGroupValuePorts valuePortGroup = valuePortContainer.getValuePortGroupByType(valuePortGroupType);
		if(valuePortGroup==null) {
			valuePortGroup = new HAPGroupValuePorts(valuePortGroupType);
			valuePortContainer.addValuePortGroup(valuePortGroup);
		}
		
		HAPValuePort internalValuePort = valuePortGroup.getValuePortByName(valuePortName);
		if(internalValuePort==null) {
			internalValuePort = new HAPValuePort(valuePortType, io);
			internalValuePort.setName(valuePortName);
			valuePortGroup.addValuePort(internalValuePort);
		}
		return internalValuePort;
	}
	

	public static HAPIdValuePortInBundle normalizeInternalValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String ioDirection, HAPWithInternalValuePort withValuePort) {
		HAPIdValuePortInBundle out = valuePortIdInBundle;
		if(out==null) {
			out = new HAPIdValuePortInBundle();
		}
		out.setValuePortSide(HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL);
		out.setValuePortId(withValuePort.getInternalValuePorts().normalizeValuePortId(out.getValuePortId(), ioDirection));
		return out;
	}

	public static HAPIdValuePortInBundle normalizeExternalValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String ioDirection, HAPWithExternalValuePort withValuePort) {
		HAPIdValuePortInBundle out = valuePortIdInBundle;
		if(out==null) {
			out = new HAPIdValuePortInBundle();
		}
		out.setValuePortSide(HAPConstantShared.VALUEPORTGROUP_SIDE_EXTERNAL);
		out.setValuePortId(withValuePort.getExternalValuePorts().normalizeValuePortId(out.getValuePortId(), ioDirection));
		return out;
	}

	public static HAPValuePort getValuePortInternal(HAPIdValuePortInBundle valuePortRef, HAPWithInternalValuePort withValuePort) {
		return withValuePort.getInternalValuePorts().getValuePort(valuePortRef.getValuePortId());
	}

	public static HAPValuePort getValuePortExternal(HAPIdValuePortInBundle valuePortRef, HAPWithExternalValuePort withValuePort) {
		return withValuePort.getExternalValuePorts().getValuePort(valuePortRef.getValuePortId());
	}
	
	public static HAPValuePort getValuePort(HAPIdValuePortInBundle valuePortRef, HAPWithBothsideValuePort withValuePort) {
		if(HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL.equals(valuePortRef.getValuePortSide())) {
			return withValuePort.getInternalValuePorts().getValuePort(valuePortRef.getValuePortId());
		}
		else {
			return withValuePort.getExternalValuePorts().getValuePort(valuePortRef.getValuePortId());
		}
	}


	
	public static HAPElementStructure getStructureElementInValuePort(String eleRootName, HAPValuePort valuePort, HAPDomainValueStructure valueStructureDomain) {
		for(String valueStructureId : valuePort.getValueStructureIds()) {
			HAPRootInStructure root = valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId).getRootByName(eleRootName);
			if(root!=null) {
				return root.getDefinition();
			}
		}
		return null;
	}
	

	public static HAPIdValuePortInBundle normalizeInBundleValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String valuePortSideIfNotProvided, String ioDirection, HAPPath blockPathFromRootIfNotProvided, HAPPath baseBlockPathFromRoot, String brickRootNameIfNotProvided, HAPBundle currentBundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPIdValuePortInBundle out = valuePortIdInBundle;
		if(out==null) {
			out = new HAPIdValuePortInBundle();
		}
		
		//normalize block id path
		HAPIdBrickInBundle brickId = out.getBrickId();
		if(brickId==null) {
			brickId = new HAPIdBrickInBundle(blockPathFromRootIfNotProvided.toString());
		}
		brickId.setIdPath(HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(brickId.getIdPath()), brickRootNameIfNotProvided, true, currentBundle).toString());
		out.setBlockId(brickId);
		
		if(out.getValuePortSide()==null) {
			//figure out value port side
			String valuePortSide;
			int k = HAPUtilityPath.comparePath(new HAPPath(brickId.getIdPath()), baseBlockPathFromRoot);
			if(k==0) {
				valuePortSide = valuePortSideIfNotProvided;
			} else if(k>0) {
				valuePortSide = HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL;
			} else {
				valuePortSide = HAPConstantShared.VALUEPORTGROUP_SIDE_EXTERNAL;
			}
			
			out.setValuePortSide(valuePortSide);
		}

		//normalize value port id
		HAPIdValuePortInBrick valuePortIdInBrick = out.getValuePortId();
		Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair = HAPUtilityBrickValuePort.getDescdentValuePortContainerInfo(currentBundle, brickRootNameIfNotProvided, new HAPPath(brickId.getIdPath()), resourceMan, runtimeInfo).getValuePortContainerPair();
		HAPContainerValuePorts valuePortContainer;
		if(HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL.equals(out.getValuePortSide())) {
			valuePortContainer = valuePortContainerPair.getLeft();
		}
		else {
			valuePortContainer = valuePortContainerPair.getRight();
		}
		
		valuePortIdInBrick = valuePortContainer.normalizeValuePortId(valuePortIdInBrick, ioDirection);
		out.setValuePortId(valuePortIdInBrick);
		
		return out;
	}


	
	public static void normalizeValuePortRelativeBrickPath(HAPIdValuePortInBundle valuePortRef, HAPPath blockPathFromRoot) {
		HAPUtilityBrickReference.calculateBrickIdInBundleRelativePath(valuePortRef.getBrickId(), blockPathFromRoot);
	}

	
	public static boolean isValuePortContainerEmpty(HAPContainerValuePorts valuePortContainer, HAPDomainValueStructure valueStructureDomain) {
		for(HAPGroupValuePorts valuePortGroup : valuePortContainer.getValuePortGroups()) {
			for(HAPValuePort valuePort : valuePortGroup.getValuePorts()) {
				for(String valueStructureId : valuePort.getValueStructureIds()) {
					if(!valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId).isEmpty()) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static HAPValuePort getInternalValuePort(HAPIdElement varId, HAPWithInternalValuePort withInternalValuePort) {
		return withInternalValuePort.getInternalValuePorts().getValuePort(varId.getRootElementId().getValuePortId().getValuePortId());
	}

}
