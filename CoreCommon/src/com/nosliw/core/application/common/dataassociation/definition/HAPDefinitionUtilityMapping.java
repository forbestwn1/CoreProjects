package com.nosliw.core.application.common.dataassociation.definition;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.common.dataassociation.HAPEndPointInTunnelConstant;
import com.nosliw.core.application.common.dataassociation.HAPEndPointInTunnelValuePort;
import com.nosliw.core.application.common.dataassociation.HAPTunnel;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafConstant;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPStructure;
import com.nosliw.core.application.common.structure.HAPUtilityElement;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.structure.reference.HAPPathElementMapping;
import com.nosliw.core.application.common.structure.reference.HAPPathElementMappingConstantToVariable;
import com.nosliw.core.application.common.structure.reference.HAPPathElementMappingVariableToVariable;
import com.nosliw.core.application.valueport.HAPIdRootElement;
import com.nosliw.core.application.valueport.HAPIdValuePortInBundle;
import com.nosliw.core.application.valueport.HAPUtilityBrickValuePort;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPDefinitionUtilityMapping {

	public static List<HAPTunnel> buildRelativePathMapping(HAPIdRootElement rootEleId, HAPElementStructure structureEle, HAPBundle bundle, String rootBrickName, HAPManagerResource resourceMan, HAPDataTypeHelper dataTypeHelper, HAPRuntimeInfo runtimeInfo){
		
		HAPIdValuePortInBundle toValuePortRef = rootEleId.getValuePortId();

		String toValueStructureId = rootEleId.getValueStructureId();
		
		HAPDomainValueStructure toValueStructureDomain = HAPUtilityBrickValuePort.getDescdentValuePortContainerInfo(bundle, rootBrickName, new HAPPath(rootEleId.getValuePortId().getBrickId().getIdPath()), resourceMan, runtimeInfo).getValueStructureDomain();		
		
		HAPStructure toValueStructure=toValueStructureDomain.getStructureDefinitionByRuntimeId(toValueStructureId);

		List<HAPTunnel> out = new ArrayList<HAPTunnel>();
		
		HAPComplexPath toItemPath = new HAPComplexPath(rootEleId.getRootName());
		HAPElementStructure toElement = HAPUtilityStructure.getDescendant(toValueStructure.getRootByName(toItemPath.getRoot()).getDefinition(), toItemPath.getPathStr());

		HAPEndPointInTunnelValuePort toEndPoint = new HAPEndPointInTunnelValuePort(toValuePortRef, toValueStructureId, toItemPath.getFullName());

		if(structureEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING)) {
			HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)structureEle;
			
			String fromValueStructureId = relativeEle.getResolveInfo().getResolvedStructureId();
			String fromItemPath = relativeEle.getResolveInfo().getResolvedElementPath().getFullName();
			
			List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
			HAPUtilityElement.mergeElement(relativeEle.getResolveInfo().getSolidElement(),  toElement, false, mappingPaths, null, dataTypeHelper);
			
			for(HAPPathElementMapping mappingPath : mappingPaths) {
				String fromItemFullPath = HAPUtilityNamingConversion.cascadePath(fromItemPath, mappingPath.getPath());
				if(mappingPath.getType().equals(HAPPathElementMapping.CONSTANT2VARIABLE)) {
					//from constant
					HAPPathElementMappingConstantToVariable mappingPath1 = (HAPPathElementMappingConstantToVariable)mappingPath;
					HAPMatchers matchers = mappingPath1.getMatcher();
					if(matchers.isVoid()) {
						matchers = null;
					}
					
					HAPEndPointInTunnelConstant fromEndPoint = new HAPEndPointInTunnelConstant(mappingPath1.getFromConstant());
					out.add(new HAPTunnel(fromEndPoint, toEndPoint, matchers));
				}
				else if(mappingPath.getType().equals(HAPPathElementMapping.VARIABLE2VARIABLE)) {
					//from variable
					HAPPathElementMappingVariableToVariable mappingPath1 = (HAPPathElementMappingVariableToVariable)mappingPath;
					HAPMatchers matchers = mappingPath1.getMatcher();
					if(matchers.isVoid()) {
						matchers = null;
					}
					HAPEndPointInTunnelValuePort fromEndPoint = new HAPEndPointInTunnelValuePort(relativeEle.getReference().getValuePortId(), fromValueStructureId, fromItemFullPath);
					out.add(new HAPTunnel(fromEndPoint, toEndPoint, matchers));
				}
			}
		}
		else if(structureEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
			HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)structureEle;
			List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
			HAPUtilityElement.mergeElement(structureEle,  toElement, false, mappingPaths, null, dataTypeHelper);
			for(HAPPathElementMapping mappingPath : mappingPaths) {
				//from constant
				HAPPathElementMappingConstantToVariable mappingPath1 = (HAPPathElementMappingConstantToVariable)mappingPath;
				HAPMatchers matchers = mappingPath1.getMatcher();
				if(matchers!=null&&matchers.isVoid()) {
					matchers = null;
				}
				HAPEndPointInTunnelConstant fromEndPoint = new HAPEndPointInTunnelConstant(mappingPath1.getFromConstant());
				out.add(new HAPTunnel(fromEndPoint, toEndPoint, matchers));
			}
		}

		return out;
	}
}
