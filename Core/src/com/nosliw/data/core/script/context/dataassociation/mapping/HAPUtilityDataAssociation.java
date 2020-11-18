package com.nosliw.data.core.script.context.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityDataAssociation {

	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionRoot contextRoot, String rootName, HAPContextStructure context){
		Map<String, Boolean> isFlatInput = new LinkedHashMap<String, Boolean>();
		isFlatInput.put(HAPConstant.NAME_DEFAULT, context.isFlat());
		Map<String, String> mapping = buildRelativePathMapping(contextRoot.getDefinition(), rootName, isFlatInput);
		Map<String, String> out = new LinkedHashMap<String, String>();
		for(String name : mapping.keySet()) {
			String mappedName = mapping.get(name);
			int index = mappedName.indexOf(".");
			mappedName = mappedName.substring(index+1);
			out.put(name, mappedName);
		}
		return out;
	}
	

	//each relative context element represent path mapping (output path in context - input path in context) during runtime
	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionRoot contextRoot, String rootName, Map<String, Boolean> isFlatInput){
		return buildRelativePathMapping(contextRoot.getDefinition(), rootName, isFlatInput);
	}
	
	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionElement contextDefEle, String rootName, Map<String, Boolean> isFlatInput){
		Map<String, String> out = new LinkedHashMap<String, String>();
		HAPUtilityContext.processContextDefElementWithPathInfo(contextDefEle, new HAPContextDefEleProcessor() {
			@Override
			public boolean process(HAPContextDefinitionElement ele, Object value) {
				String path = (String)value;
				if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)ele;
					HAPContextPath contextPath = relativeEle.getPath();
					String parent = relativeEle.getParent();
					String sourcePath = HAPNamingConversionUtility.cascadePath(parent, isFlatInput.get(parent)? contextPath.getFullPath() : contextPath.getContextFullPath());
					String targetPath = path; 
					out.put(targetPath, sourcePath);
					return false;
				}
				return true;
			}

			@Override
			public boolean postProcess(HAPContextDefinitionElement ele, Object value) {
				return true;
			}}, rootName);
		return out;
	}
	
	public static HAPConfigureContextProcessor getContextProcessConfigurationForProcess() {
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE;
		return configure;
	}

}
