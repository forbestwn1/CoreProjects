package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityDataAssociation {

	public static Map<String, HAPDefinitionDataAssociation> buildDataAssociation(JSONObject daJsonObj){
		//seperate by target name
		Map<String, JSONObject> jsonMapByTarget = new LinkedHashMap<String, JSONObject>();
		for(Object key : daJsonObj.keySet()) {
			HAPTarget target = new HAPTarget((String)key);
			String targetName = target.getTargetName();
			JSONObject jsonByTarget = jsonMapByTarget.get(targetName);
			if(jsonByTarget!=null) {
				jsonByTarget = new JSONObject();
				jsonMapByTarget.put(targetName, jsonByTarget);
			}
			jsonByTarget.put(target.getRootNodeId().getFullName(), daJsonObj.optJSONObject((String)key));
		}
		
		Map<String, HAPDefinitionDataAssociation> out = new LinkedHashMap<String, HAPDefinitionDataAssociation>();
		for(String targetName : jsonMapByTarget.keySet()) {
			HAPDefinitionDataAssociation dataAssociation = new HAPDefinitionDataAssociation();
			dataAssociation.buildObject(jsonMapByTarget.get(targetName), HAPSerializationFormat.JSON);
			out.put(targetName, dataAssociation);
		}
		return out;
	}
	
	//each relative context element represent path mapping (output path in context - input path in context) during runtime
	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionRoot contextRoot, String rootName, boolean isFlatInput){
		return buildRelativePathMapping(contextRoot.getDefinition(), rootName, isFlatInput);
	}
	
	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionElement contextDefEle, String rootName, boolean isFlatInput){
		Map<String, String> out = new LinkedHashMap<String, String>();
		HAPUtilityContext.processContextDefElementWithPathInfo(contextDefEle, new HAPContextDefEleProcessor() {
			@Override
			public boolean process(HAPContextDefinitionElement ele, Object value) {
				String path = (String)value;
				if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)ele;
					HAPContextPath contextPath = relativeEle.getPath();
					String sourcePath = isFlatInput? contextPath.getFullPath() : contextPath.getContextFullPath();
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
