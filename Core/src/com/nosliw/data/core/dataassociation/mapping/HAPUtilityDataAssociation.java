package com.nosliw.data.core.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafConstant;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.temp.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;

public class HAPUtilityDataAssociation {

	public static Map<String, String> buildSimplifiedRelativePathMapping(HAPRoot contextRoot, String rootName, HAPValueStructureDefinition context){
		Map<String, Boolean> isFlatInput = new LinkedHashMap<String, Boolean>();
		if(context!=null)	isFlatInput.put(HAPConstantShared.NAME_DEFAULT, context.isFlat());
		else isFlatInput.put(HAPConstantShared.NAME_DEFAULT, true);
		Map<String, String> mapping = buildRelativePathMapping(contextRoot, rootName, isFlatInput);
		Map<String, String> out = new LinkedHashMap<String, String>();
		for(String targetPath : mapping.keySet()) {
			//get rid of parent part in source path
			String sourcePath = mapping.get(targetPath);
			int index = sourcePath.indexOf(".");
			sourcePath = sourcePath.substring(index+1);
			out.put(targetPath, sourcePath);
		}
		return out;
	}
	
	public static Map<String, String> buildSimplifiedRelativePathMapping(HAPRoot contextRoot, String rootName){
		return buildSimplifiedRelativePathMapping(contextRoot, rootName, null);
	}

	//each relative context element represent path mapping (output path in context - input path in context) during runtime
	public static Map<String, String> buildRelativePathMapping(HAPRoot contextRoot, String rootName, Map<String, Boolean> isFlatInput){
		Map<String, String> out = new LinkedHashMap<String, String>();
		HAPUtilityContext.processContextRootElement(contextRoot, rootName, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)eleInfo.getElement();
					HAPReferenceElement contextPath = relativeEle.getPathFormat();
					String parent = relativeEle.getParent();
					String sourcePath = HAPNamingConversionUtility.cascadePath(parent, isFlatInput.get(parent)? contextPath.getFullPath() : contextPath.getContextFullPath());
					out.put(eleInfo.getElementPath().getFullPath(), sourcePath);
					return Pair.of(false, null);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}}, null);
		return out;
	}
	
	//build constant assignment mapping
	public static Map<String, Object> buildConstantAssignment(HAPRoot contextRoot, String rootName, Map<String, Boolean> isFlatInput){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		HAPUtilityContext.processContextRootElement(contextRoot, rootName, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
					HAPElementLeafConstant constantEle = (HAPElementLeafConstant)eleInfo.getElement();
					out.put(eleInfo.getElementPath().getFullPath(), constantEle.getValue());
					return Pair.of(false, null);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {		}}, null);
		return out;
	}
	
	public static HAPConfigureProcessorStructure getContextProcessConfigurationForDataAssociation(HAPInfo daProcessConfigure) {
		HAPConfigureProcessorStructure configure = new HAPConfigureProcessorStructure();
		configure.inheritMode = HAPConstant.INHERITMODE_NONE;
		if(HAPUtilityDAProcess.ifModifyInputStructure(daProcessConfigure))  configure.tolerantNoParentForRelative = true;
		return configure;
	} 

}
