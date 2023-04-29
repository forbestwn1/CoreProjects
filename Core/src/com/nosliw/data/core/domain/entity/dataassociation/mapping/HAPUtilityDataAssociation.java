package com.nosliw.data.core.domain.entity.dataassociation.mapping;

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
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.temp.HAPProcessorContextDefinitionElement;

public class HAPUtilityDataAssociation {

	public static List<HAPPathValueMapping> buildRelativePathMapping(HAPItemValueMapping<HAPIdRootElement> valueMappingItem, HAPDomainValueStructure inValueStructureDomain, HAPRuntimeEnvironment runtimeEnv){
		List<HAPPathValueMapping> out = new ArrayList<HAPPathValueMapping>();
		HAPUtilityStructure.traverseElement(valueMappingItem.getDefinition(), valueMappingItem.getTarget().getRootName(), new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING)) {
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
					
					String toValueStructureId = valueMappingItem.getTarget().getValueStructureId();
					HAPComplexPath toItemPath = new HAPComplexPath(eleInfo.getElementPath().getFullName());
					
					String fromValueStructureId = relativeEle.getResolveInfo().getResolvedStructureId();
					String fromItemPath = relativeEle.getResolveInfo().getResolvedElementPath().getFullName();
					
					HAPDefinitionEntityValueStructure toValueStructure = inValueStructureDomain.getValueStructureDefInfoByRuntimeId(toValueStructureId).getValueStructure();
					HAPElementStructure toElement = HAPUtilityStructure.getDescendant(toValueStructure.getRootByName(toItemPath.getRoot()).getDefinition(), toItemPath.getPathStr());
					
					Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
					HAPUtilityStructure.mergeElement(relativeEle.getResolveInfo().getSolidElement(),  toElement, false, matchers, null, runtimeEnv);
					
					for(String path : matchers.keySet()) {
						HAPMatchers matcher = matchers.get(path);
						out.add(new HAPPathValueMapping(relativeEle.getReference().getParentValueContextName(), fromValueStructureId, HAPUtilityNamingConversion.cascadePath(fromItemPath, path), matcher.isVoid()?null:matcher, valueMappingItem.getTarget().getDomainId(), toValueStructureId, HAPUtilityNamingConversion.cascadePath(toItemPath.getFullName(), path)));
					}
					
					return Pair.of(false, null);
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
		HAPUtilityStructure.traverseElement(valueMappingItem.getDefinition(), valueMappingItem.getTarget().getPath().getFullName(), new HAPProcessorContextDefinitionElement() {
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
		HAPUtilityStructure.traverseElement(root, rootName, new HAPProcessorContextDefinitionElement() {
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
		HAPUtilityStructure.traverseElement(contextRoot, rootName, new HAPProcessorContextDefinitionElement() {
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
