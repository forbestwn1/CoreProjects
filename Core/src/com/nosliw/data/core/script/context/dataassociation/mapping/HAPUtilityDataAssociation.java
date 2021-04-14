package com.nosliw.data.core.script.context.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafConstant;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPInfoContextNode;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPUtilityDAProcess;

public class HAPUtilityDataAssociation {

	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionRoot contextRoot, String rootName, HAPContextStructure context){
		Map<String, Boolean> isFlatInput = new LinkedHashMap<String, Boolean>();
		isFlatInput.put(HAPConstantShared.NAME_DEFAULT, context.isFlat());
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
	

	//each relative context element represent path mapping (output path in context - input path in context) during runtime
	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionRoot contextRoot, String rootName, Map<String, Boolean> isFlatInput){
		Map<String, String> out = new LinkedHashMap<String, String>();
		HAPUtilityContext.processContextRootElement(contextRoot, rootName, new HAPContextDefEleProcessor() {
			@Override
			public Pair<Boolean, HAPContextDefinitionElement> process(HAPInfoContextNode eleInfo, Object value) {
				if(eleInfo.getContextElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)eleInfo.getContextElement();
					HAPContextPath contextPath = relativeEle.getPath();
					String parent = relativeEle.getParent();
					String sourcePath = HAPNamingConversionUtility.cascadePath(parent, isFlatInput.get(parent)? contextPath.getFullPath() : contextPath.getContextFullPath());
					out.put(eleInfo.getContextPath().getFullPath(), sourcePath);
					return Pair.of(false, null);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoContextNode eleInfo, Object value) {	}}, null);
		return out;
	}
	
	//build constant assignment mapping
	public static Map<String, Object> buildConstantAssignment(HAPContextDefinitionRoot contextRoot, String rootName, Map<String, Boolean> isFlatInput){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		HAPUtilityContext.processContextRootElement(contextRoot, rootName, new HAPContextDefEleProcessor() {
			@Override
			public Pair<Boolean, HAPContextDefinitionElement> process(HAPInfoContextNode eleInfo, Object value) {
				if(eleInfo.getContextElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
					HAPContextDefinitionLeafConstant constantEle = (HAPContextDefinitionLeafConstant)eleInfo.getContextElement();
					out.put(eleInfo.getContextPath().getFullPath(), constantEle.getValue());
					return Pair.of(false, null);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoContextNode eleInfo, Object value) {		}}, null);
		return out;
	}
	
	public static HAPConfigureContextProcessor getContextProcessConfigurationForDataAssociation(HAPInfo daProcessConfigure) {
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPConstant.INHERITMODE_NONE;
		if(HAPUtilityDAProcess.ifModifyInputStructure(daProcessConfigure))  configure.tolerantNoParentForRelative = true;
		return configure;
	} 

}
