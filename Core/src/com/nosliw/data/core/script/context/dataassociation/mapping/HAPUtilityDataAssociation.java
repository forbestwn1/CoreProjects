package com.nosliw.data.core.script.context.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
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
		HAPUtilityContext.processContextDefElement(new HAPInfoContextNode(contextDefEle, new HAPContextPath(rootName)), new HAPContextDefEleProcessor() {
			@Override
			public boolean process(HAPInfoContextNode eleInfo, Object value) {
				if(eleInfo.getContextElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)eleInfo.getContextElement();
					HAPContextPath contextPath = relativeEle.getPath();
					String parent = relativeEle.getParent();
					String sourcePath = HAPNamingConversionUtility.cascadePath(parent, isFlatInput.get(parent)? contextPath.getFullPath() : contextPath.getContextFullPath());
					out.put(eleInfo.getContextPath().getFullPath(), sourcePath);
					return false;
				}
				return true;
			}

			@Override
			public boolean postProcess(HAPInfoContextNode eleInfo, Object value) {
				return true;
			}}, null);
		return out;
	}
	
	public static HAPConfigureContextProcessor getContextProcessConfigurationForDataAssociation(HAPInfo daProcessConfigure) {
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPConstant.INHERITMODE_NONE;
		if(HAPUtilityDAProcess.ifModifyInputStructure(daProcessConfigure))  configure.tolerantNoParentForRelative = true;
		return configure;
	} 

}
