package com.nosliw.core.application.common.valueport;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.valuestructure.HAPDefinitionStructure;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityValuePort {

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
	
	public static HAPElementStructure getInternalElement(HAPIdElement varId, HAPDomainValueStructure valueStructureDomain) {
		HAPDefinitionStructure structureDef = valueStructureDomain.getStructureDefinitionByRuntimeId(varId.getRootElementId().getValueStructureId());
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

	
	public static HAPIdValuePortInBundle normalizeInBundleValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String ioDirection, HAPPath blockPathFromRoot, HAPBundle currentBundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
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
		valuePortIdInBrick = HAPUtilityBrick.getDescdentValuePortContainerInfo(currentBundle, new HAPPath(brickId.getIdPath()), resourceMan, runtimeInfo).getValuePortContainer().normalizeValuePortId(valuePortIdInBrick, ioDirection);
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
		HAPInfoValuePortContainer valuePortContainerInfo = HAPUtilityBrick.getDescdentValuePortContainerInfo(bundle, new HAPPath(valuePortRef.getBrickId().getIdPath()), resourceMan, runtimeInfo);
		return new HAPInfoValuePort(valuePortContainerInfo.getValuePortContainer().getValuePort(valuePortRef.getValuePortId()), valuePortContainerInfo.getValueStructureDomain());
	}
	
}
