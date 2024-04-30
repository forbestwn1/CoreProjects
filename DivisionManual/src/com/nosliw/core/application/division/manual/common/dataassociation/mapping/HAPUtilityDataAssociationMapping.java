package com.nosliw.core.application.division.manual.common.dataassociation.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPReferenceBrickLocal;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPEndPointInTunnelConstant;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPEndPointInTunnelValuePort;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPTunnel;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafConstant;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPPathElementMapping;
import com.nosliw.core.application.common.structure.HAPPathElementMappingConstantToVariable;
import com.nosliw.core.application.common.structure.HAPPathElementMappingVariableToVariable;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.valueport.HAPIdValuePort;
import com.nosliw.core.application.common.valueport.HAPReferenceValuePort;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort;
import com.nosliw.core.application.common.variable.HAPIdRootElement;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityDataAssociationMapping {

	public static List<HAPTunnel> buildRelativePathMapping(HAPIdRootElement rootEleId, HAPElementStructure structureEle, HAPBundle bundle, HAPRuntimeEnvironment runtimeEnv){
		
		HAPReferenceValuePort toValuePortRef = rootEleId.getValuePortRef();
		Triple<HAPReferenceBrickLocal, HAPIdValuePort, HAPValuePort> toValuePortInfo = HAPUtilityValuePort.getValuePort(toValuePortRef, null, bundle);
		HAPValuePort toValuePort = toValuePortInfo.getRight();

		String toValueStructureId = rootEleId.getValueStructureId();
		HAPValueStructureInValuePort toValueStructure = toValuePort.getValueStructureDefintion(toValueStructureId); 

		
		List<HAPTunnel> out = new ArrayList<HAPTunnel>();
		HAPUtilityStructure.traverseElement(structureEle, rootEleId.getRootName(), new HAPProcessorStructureElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				HAPComplexPath toItemPath = new HAPComplexPath(eleInfo.getElementPath().getFullName());
				HAPElementStructure toElement = HAPUtilityStructure.getDescendant(toValueStructure.getRootByName(toItemPath.getRoot()).getDefinition(), toItemPath.getPathStr());

				HAPEndPointInTunnelValuePort toEndPoint = new HAPEndPointInTunnelValuePort(toValuePortRef, toValueStructureId, toItemPath.getFullName());

				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING)) {
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
					
					String fromValueStructureId = relativeEle.getResolveInfo().getResolvedStructureId();
					String fromItemPath = relativeEle.getResolveInfo().getResolvedElementPath().getFullName();
					
					List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
					HAPUtilityStructure.mergeElement(relativeEle.getResolveInfo().getSolidElement(),  toElement, false, mappingPaths, null, runtimeEnv);
					
					for(HAPPathElementMapping mappingPath : mappingPaths) {
						String fromItemFullPath = HAPUtilityNamingConversion.cascadePath(fromItemPath, mappingPath.getPath());
						String toItemFullPath = HAPUtilityNamingConversion.cascadePath(toItemPath.getFullName(), mappingPath.getPath());
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
							HAPEndPointInTunnelValuePort fromEndPoint = new HAPEndPointInTunnelValuePort(relativeEle.getReference().getValuePortRef(), fromValueStructureId, fromItemFullPath);
							out.add(new HAPTunnel(fromEndPoint, toEndPoint, matchers));
						}
					}
					
					return Pair.of(false, null);
				}
				else if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
				HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)eleInfo.getElement();
					List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
					HAPUtilityStructure.mergeElement(eleInfo.getElement(),  toElement, false, mappingPaths, null, runtimeEnv);
					for(HAPPathElementMapping mappingPath : mappingPaths) {
						//from constant
						HAPPathElementMappingConstantToVariable mappingPath1 = (HAPPathElementMappingConstantToVariable)mappingPath;
						HAPMatchers matchers = mappingPath1.getMatcher();
						if(matchers.isVoid()) {
							matchers = null;
						}
						HAPEndPointInTunnelConstant fromEndPoint = new HAPEndPointInTunnelConstant(mappingPath1.getFromConstant());
						out.add(new HAPTunnel(fromEndPoint, toEndPoint, matchers));
					}
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}}, null);

		return out;
	}
}
