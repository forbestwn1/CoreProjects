package com.nosliw.core.application.common.valueport;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.HAPUtilityBrickValuePort;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPStructureImp;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityValuePort {

	public static Pair<HAPValuePort, HAPValuePort> getOrCreateValuePort(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainers, String valuePortGroupType, String valuePortType, String valuePortName, Pair<String, String> io){
		HAPContainerValuePorts internalValuePortContainer = valuePortContainers.getLeft();
		HAPContainerValuePorts externalValuePortContainer = valuePortContainers.getRight();
		
		HAPGroupValuePorts internalValuePortGroup = internalValuePortContainer.getValuePortGroupByType(valuePortGroupType);
		HAPGroupValuePorts externalValuePortGroup = externalValuePortContainer.getValuePortGroupByType(valuePortGroupType);
		if(internalValuePortGroup==null) {
			internalValuePortGroup = new HAPGroupValuePorts(valuePortGroupType);
			externalValuePortGroup = new HAPGroupValuePorts(valuePortGroupType);
			internalValuePortContainer.addValuePortGroup(internalValuePortGroup);
			externalValuePortContainer.addValuePortGroup(externalValuePortGroup);
		}
		
		HAPValuePort internalValuePort = internalValuePortGroup.getValuePortByName(valuePortName);
		HAPValuePort externalValuePort = externalValuePortGroup.getValuePortByName(valuePortName);
		if(internalValuePort==null) {
			internalValuePort = new HAPValuePort(valuePortType, io.getLeft());
			internalValuePort.setName(valuePortName);
			internalValuePortGroup.addValuePort(internalValuePort);
			externalValuePort = new HAPValuePort(valuePortType, io.getRight());
			externalValuePort.setName(valuePortName);
			externalValuePortGroup.addValuePort(externalValuePort);
		}
		return Pair.of(internalValuePort, externalValuePort);
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
	
	public static HAPElementStructure getStructureElementInValuePort(String eleRootName, HAPValuePort valuePort, HAPDomainValueStructure valueStructureDomain) {
		for(String valueStructureId : valuePort.getValueStructureIds()) {
			HAPRootInStructure root = valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId).getRootByName(eleRootName);
			if(root!=null) {
				return root.getDefinition();
			}
		}
		return null;
	}
	
	public static HAPElementStructure getInternalElement(HAPIdElement varId, HAPDomainValueStructure valueStructureDomain) {
		HAPStructureImp structureDef = valueStructureDomain.getStructureDefinitionByRuntimeId(varId.getRootElementId().getValueStructureId());
		HAPElementStructure structureEle = HAPUtilityStructure.getDescendant(structureDef.getRootByName(varId.getRootElementId().getRootName()).getDefinition(), varId.getElementPath().toString());
		return structureEle;
	}
	
	public static HAPElementStructure getInternalElement(HAPIdElement varId, HAPWithInternalValuePort withInternalValuePort) {
		HAPValuePort valuePort = getInternalValuePort(varId, withInternalValuePort);
		HAPValueStructureInValuePort11111 valueStructureInValuePort = valuePort.getValueStructureDefintion(varId.getRootElementId().getValueStructureId());
		HAPElementStructure structureEle = HAPUtilityStructure.getDescendant(valueStructureInValuePort.getRoot(varId.getRootElementId().getRootName()).getDefinition(), varId.getElementPath().toString());
		return structureEle;
	}
	
	public static HAPValuePort getInternalValuePort(HAPIdElement varId, HAPWithInternalValuePort withInternalValuePort) {
		return withInternalValuePort.getInternalValuePorts().getValuePort(varId.getRootElementId().getValuePortId().getValuePortId());
	}

	
	public static HAPIdValuePortInBundle normalizeInBundleValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String valueGroupContainerSide, String ioDirection, HAPPath blockPathFromRoot, String brickRootNameIfNotProvided, HAPBundle currentBundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPIdValuePortInBundle out = valuePortIdInBundle;
		if(out==null) {
			out = new HAPIdValuePortInBundle();
		}
		
		//normalize block reference
		HAPIdBrickInBundle brickId = out.getBrickId();
		if(brickId==null) {
			brickId = new HAPIdBrickInBundle(blockPathFromRoot.toString());
			out.setBlockId(brickId);
		}

		//normalize value port id
		HAPIdValuePortInBrick valuePortIdInBrick = out.getValuePortId();
		Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair = HAPUtilityBrickValuePort.getDescdentValuePortContainerInfo(currentBundle, brickRootNameIfNotProvided, new HAPPath(brickId.getIdPath()), resourceMan, runtimeInfo).getValuePortContainerPair();
		HAPContainerValuePorts valuePortContainer;
		if(valueGroupContainerSide.equals(HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL)) {
			valuePortContainer = valuePortContainerPair.getLeft();
		}
		else {
			valuePortContainer = valuePortContainerPair.getRight();
		}
		
		valuePortIdInBrick = valuePortContainer.normalizeValuePortId(valuePortIdInBrick, ioDirection);
		out.setValuePortId(valuePortIdInBrick);
		
		return out;
	}

	public static HAPIdValuePortInBundle normalizeInternalValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String ioDirection, HAPWithInternalValuePort withValuePort) {
		HAPIdValuePortInBundle out = valuePortIdInBundle;
		if(out==null) {
			out = new HAPIdValuePortInBundle();
		}
		out.setValuePortId(withValuePort.getInternalValuePorts().normalizeValuePortId(out.getValuePortId(), ioDirection));
		return out;
	}

	public static HAPIdValuePortInBundle normalizeExternalValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String ioDirection, HAPWithExternalValuePort withValuePort) {
		HAPIdValuePortInBundle out = valuePortIdInBundle;
		if(out==null) {
			out = new HAPIdValuePortInBundle();
		}
		out.setValuePortId(withValuePort.getExternalValuePorts().normalizeValuePortId(out.getValuePortId(), ioDirection));
		return out;
	}

	public static void normalizeValuePortRelativeBrickPath(HAPIdValuePortInBundle valuePortRef, HAPPath blockPathFromRoot) {
		HAPIdBrickInBundle brickRef = valuePortRef.getBrickId();
		if(brickRef.getRelativePath()==null) {
			brickRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickRef.getIdPath(), blockPathFromRoot.toString()));
		}
	}

	public static HAPValuePort getValuePortInternal(HAPIdValuePortInBundle valuePortRef, HAPWithInternalValuePort withValuePort) {
		return withValuePort.getInternalValuePorts().getValuePort(valuePortRef.getValuePortId());
	}

	public static HAPValuePort getValuePortExternal(HAPIdValuePortInBundle valuePortRef, HAPWithExternalValuePort withValuePort) {
		return withValuePort.getExternalValuePorts().getValuePort(valuePortRef.getValuePortId());
	}

	public static HAPInfoValuePort getValuePortInBundle(HAPIdValuePortInBundle valuePortRef, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPInfoValuePortContainer valuePortContainerInfo = HAPUtilityBrickValuePort.getDescdentValuePortContainerInfo(bundle, null, new HAPPath(valuePortRef.getBrickId().getIdPath()), resourceMan, runtimeInfo);
		return new HAPInfoValuePort(valuePortContainerInfo.getValuePortContainerPair().getRight().getValuePort(valuePortRef.getValuePortId()), valuePortContainerInfo.getValueStructureDomain());
	}
	
}
