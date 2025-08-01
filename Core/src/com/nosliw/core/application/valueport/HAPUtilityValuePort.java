package com.nosliw.core.application.valueport;

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
import com.nosliw.core.xxx.application1.brick.HAPUtilityBrickPath;
import com.nosliw.core.xxx.application1.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityValuePort {

	public static Pair<HAPValuePort, HAPValuePort> getOrCreateValuePort(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainers, String valuePortGroupType, String valuePortType, String valuePortName, Pair<String, String> io){
		
		return Pair.of(
				valuePortContainers.getLeft()==null? null : getOrCreateValuePort(valuePortContainers.getLeft(), valuePortGroupType, valuePortType, valuePortName, io.getLeft()),
				valuePortContainers.getRight()==null? null : getOrCreateValuePort(valuePortContainers.getRight(), valuePortGroupType, valuePortType, valuePortName, io.getRight()));
		
//		HAPContainerValuePorts internalValuePortContainer = valuePortContainers.getLeft();
//		HAPContainerValuePorts externalValuePortContainer = valuePortContainers.getRight();
//		
//		HAPGroupValuePorts internalValuePortGroup = internalValuePortContainer.getValuePortGroupByType(valuePortGroupType);
//		HAPGroupValuePorts externalValuePortGroup = externalValuePortContainer.getValuePortGroupByType(valuePortGroupType);
//		if(internalValuePortGroup==null) {
//			internalValuePortGroup = new HAPGroupValuePorts(valuePortGroupType);
//			externalValuePortGroup = new HAPGroupValuePorts(valuePortGroupType);
//			internalValuePortContainer.addValuePortGroup(internalValuePortGroup);
//			externalValuePortContainer.addValuePortGroup(externalValuePortGroup);
//		}
//		
//		HAPValuePort internalValuePort = internalValuePortGroup.getValuePortByName(valuePortName);
//		HAPValuePort externalValuePort = externalValuePortGroup.getValuePortByName(valuePortName);
//		if(internalValuePort==null) {
//			internalValuePort = new HAPValuePort(valuePortType, io.getLeft());
//			internalValuePort.setName(valuePortName);
//			internalValuePortGroup.addValuePort(internalValuePort);
//			externalValuePort = new HAPValuePort(valuePortType, io.getRight());
//			externalValuePort.setName(valuePortName);
//			externalValuePortGroup.addValuePort(externalValuePort);
//		}
//		return Pair.of(internalValuePort, externalValuePort);
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
	
	public static HAPValuePort getValuePort(HAPIdValuePortInBundle valuePortRef, HAPWithBothsideValuePort withValuePort) {
		if(HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL.equals(valuePortRef.getValuePortSide())) {
			return withValuePort.getInternalValuePorts().getValuePort(valuePortRef.getValuePortId());
		}
		else {
			return withValuePort.getExternalValuePorts().getValuePort(valuePortRef.getValuePortId());
		}
	}

	public static HAPInfoValuePort getValuePortInBundle(HAPIdValuePortInBundle valuePortRef, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPInfoValuePortContainer valuePortContainerInfo = HAPUtilityBrickValuePort.getDescdentValuePortContainerInfo(bundle, null, new HAPPath(valuePortRef.getBrickId().getIdPath()), resourceMan, runtimeInfo);
		HAPContainerValuePorts valuePortContainer;
		if(HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL.equals(valuePortRef.getValuePortSide())) {
			valuePortContainer = valuePortContainerInfo.getValuePortContainerPair().getLeft();
		}
		else {
			valuePortContainer = valuePortContainerInfo.getValuePortContainerPair().getRight();
		}
		return new HAPInfoValuePort(valuePortContainer.getValuePort(valuePortRef.getValuePortId()), valuePortContainerInfo.getValueStructureDomain());
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

	
	
}
