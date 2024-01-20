package com.nosliw.data.core.dataassociation.mapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.domain.valueport.HAPValuePort;
import com.nosliw.data.core.domain.valueport.HAPIdValuePort;
import com.nosliw.data.core.domain.valueport.HAPUtilityValuePort;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafProvide;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPPathElementMapping;
import com.nosliw.data.core.structure.HAPPathElementMappingConstantToVariable;
import com.nosliw.data.core.structure.HAPPathElementMappingVariableToVariable;
import com.nosliw.data.core.structure.HAPProcessorStructureElement;
import com.nosliw.data.core.structure.HAPUtilityStructure;

public class HAPUtilityDataAssociation {

	public static List<HAPPathValueMapping> buildRelativePathMapping(HAPItemValueMapping<HAPIdRootElement> valueMappingItem, HAPContextProcessor processContext){
		HAPIdValuePort valuePortId = valueMappingItem.getTarget().getValuePortId();
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePort(valuePortId, processContext); 
		
		List<HAPPathValueMapping> out = new ArrayList<HAPPathValueMapping>();
		HAPUtilityStructure.traverseElement(valueMappingItem.getDefinition(), valueMappingItem.getTarget().getRootName(), new HAPProcessorStructureElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				String toValueStructureId = valueMappingItem.getTarget().getValueStructureId();
				HAPComplexPath toItemPath = new HAPComplexPath(eleInfo.getElementPath().getFullName());
					
				HAPDefinitionEntityValueStructure toValueStructure = valuePort.getValueStructureDefintion(toValueStructureId); 
//						inValueStructureDomain.getValueStructureDefInfoByRuntimeId(toValueStructureId).getValueStructure();
				HAPElementStructure toElement = HAPUtilityStructure.getDescendant(toValueStructure.getRootByName(toItemPath.getRoot()).getDefinition(), toItemPath.getPathStr());
				
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING)) {
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
					
					String fromValueStructureId = relativeEle.getResolveInfo().getResolvedStructureId();
					String fromItemPath = relativeEle.getResolveInfo().getResolvedElementPath().getFullName();
					
					List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
					HAPUtilityStructure.mergeElement(relativeEle.getResolveInfo().getSolidElement(),  toElement, false, mappingPaths, null, processContext.getRuntimeEnvironment());
					
					for(HAPPathElementMapping mappingPath : mappingPaths) {
						String fromItemFullPath = HAPUtilityNamingConversion.cascadePath(fromItemPath, mappingPath.getPath());
						String toItemFullPath = HAPUtilityNamingConversion.cascadePath(toItemPath.getFullName(), mappingPath.getPath());
						if(mappingPath.getType().equals(HAPPathElementMapping.CONSTANT2VARIABLE)) {
							//from constant
							HAPPathElementMappingConstantToVariable mappingPath1 = (HAPPathElementMappingConstantToVariable)mappingPath;
							HAPMatchers matchers = mappingPath1.getMatcher();
							if(matchers.isVoid())  matchers = null;
							out.add(new HAPPathValueMapping(mappingPath1.getFromConstant(), matchers, valuePortId, toValueStructureId, toItemFullPath));
						}
						else if(mappingPath.getType().equals(HAPPathElementMapping.VARIABLE2VARIABLE)) {
							//from variable
							HAPPathElementMappingVariableToVariable mappingPath1 = (HAPPathElementMappingVariableToVariable)mappingPath;
							HAPMatchers matchers = mappingPath1.getMatcher();
							if(matchers.isVoid())  matchers = null;
							out.add(new HAPPathValueMapping(relativeEle.getReference().getValuePortId(), fromValueStructureId, fromItemFullPath, matchers, valuePortId, toValueStructureId, toItemFullPath));
						}
					}
					
					return Pair.of(false, null);
				}
				else if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_PROVIDE)) {
					HAPElementStructureLeafProvide provideEle = (HAPElementStructureLeafProvide)eleInfo.getElement();
					List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
					HAPUtilityStructure.mergeElement(provideEle.getDefinition(),  toElement, false, mappingPaths, null, processContext.getRuntimeEnvironment());
					for(HAPPathElementMapping mappingPath : mappingPaths) {
						HAPPathElementMappingVariableToVariable mappingPath1 = (HAPPathElementMappingVariableToVariable)mappingPath;
						HAPMatchers matchers = mappingPath1.getMatcher();
						if(matchers.isVoid())  matchers = null;
						out.add(new HAPPathValueMapping(provideEle.getName(), mappingPath1.getPath(), matchers, valueMappingItem.getTarget().getValuePortId(), toValueStructureId, toItemPath.getFullName()));
					}
				}
				else if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
				HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)eleInfo.getElement();
					List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
					HAPUtilityStructure.mergeElement(eleInfo.getElement(),  toElement, false, mappingPaths, null, processContext.getRuntimeEnvironment());
					for(HAPPathElementMapping mappingPath : mappingPaths) {
						//from constant
						HAPPathElementMappingConstantToVariable mappingPath1 = (HAPPathElementMappingConstantToVariable)mappingPath;
						HAPMatchers matchers = mappingPath1.getMatcher();
						if(matchers.isVoid())  matchers = null;
						String toItemFullPath = HAPUtilityNamingConversion.cascadePath(toItemPath.getFullName(), mappingPath.getPath());
						out.add(new HAPPathValueMapping(mappingPath1.getFromConstant(), matchers, valueMappingItem.getTarget().getValuePortId(), toValueStructureId, toItemFullPath));
					}
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}}, null);
		return out;
	}
	
	//each relative context element represent path mapping (output path in context - input path in context) during runtime
	public static Map<String, String> buildRelativePathMapping1(HAPItemValueMapping<HAPIdRootElement> valueMappingItem){
		Map<String, String> out = new LinkedHashMap<String, String>();
		HAPUtilityStructure.traverseElement(valueMappingItem.getDefinition(), valueMappingItem.getTarget().getPath().getFullName(), new HAPProcessorStructureElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING)) {
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
					HAPComplexPath sourcePath = new HAPComplexPath(relativeEle.getResolveInfo().getResolvedStructureId(), relativeEle.getResolveInfo().getResolvedElementPath().getFullName());
					out.put(eleInfo.getElementPath().getFullName(), sourcePath.getFullName());
					return Pair.of(false, null);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}}, null);
		return out;
	}

	//each relative context element represent path mapping (output path in context - input path in context) during runtime
	public static Map<String, String> buildRelativePathMapping1(HAPRootStructure root, String rootName){
		Map<String, String> out = new LinkedHashMap<String, String>();
		HAPUtilityStructure.traverseElement(root, rootName, new HAPProcessorStructureElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
					HAPComplexPath contextPath = relativeEle.getResolvedIdPath();
					String parent = relativeEle.getReference().getParentValueContextName();
					String sourcePath = HAPUtilityNamingConversion.cascadePath(parent, contextPath.getFullName());
					out.put(eleInfo.getElementPath().getFullName(), sourcePath);
					return Pair.of(false, null);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}}, null);
		return out;
	}
	
	//build constant assignment mapping
	public static Map<String, Object> buildConstantAssignment(HAPRootStructure contextRoot, String rootName){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		HAPUtilityStructure.traverseElement(contextRoot, rootName, new HAPProcessorStructureElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
					HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)eleInfo.getElement();
					out.put(eleInfo.getElementPath().getFullName(), constantEle.getValue());
					return Pair.of(false, null);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {		}}, null);
		return out;
	}
	
	public static HAPConfigureProcessorValueStructure getContextProcessConfigurationForDataAssociation(HAPInfo daProcessConfigure) {
		HAPConfigureProcessorValueStructure configure = new HAPConfigureProcessorValueStructure();
		configure.inheritMode = HAPConstant.INHERITMODE_NONE;
		if(HAPUtilityDAProcess.ifModifyInputStructure(daProcessConfigure))  configure.tolerantNoParentForRelative = true;
		return configure;
	} 

}
