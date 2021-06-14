package com.nosliw.data.core.structure;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.structure.temp.HAPUtilityContextInfo;
import com.nosliw.data.core.valuestructure.HAPExecutableValueStructure;

public class HAPUtilityStructureScript {

	public static HAPJsonTypeScript buildContextInitScript(HAPExecutableValueStructure valueStructure) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		//build init output object 
		JSONObject output = HAPUtilityStructureScript.buildDefaultJsonObject(valueStructure);
		templateParms.put("outputInit", HAPJsonUtility.formatJson(output.toString()));

		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityStructureScript.class, "ContextInitFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPJsonTypeScript(script);
	}
	
	//build default value structure for executable value structure
	public static JSONObject buildDefaultJsonObject(HAPExecutableValueStructure valueStructure) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		for(HAPRootStructure root : valueStructure.getAllRoots()) {
			Object value = root.getDefaultValue();
			if(value!=null) {
				jsonMap.put(root.getLocalId(), value.toString());
			}
		}
		return new JSONObject(HAPJsonUtility.buildMapJson(jsonMap));
	}

	//build skeleton, it is used for data mapping operation
	public static JSONObject buildSkeletonJsonObject(HAPExecutableValueStructure valueStructure) {
		JSONObject output = new JSONObject();
		for(HAPRootStructure root : valueStructure.getAllRoots()) {
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				HAPElementStructure eleStructure = root.getDefinition();
				Object eleStructureJson = buildJsonValue(eleStructure);
				JSONObject parentJsonObj = output;
				parentJsonObj.put(root.getLocalId(), eleStructureJson);
			}
		}
		return output;
	}
	
	private static Object buildJsonValue(HAPElementStructure contextDefEle) {
		switch(contextDefEle.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT:
		{
			HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)contextDefEle;
			return constantEle.getValue();
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
		{
			HAPElementStructureNode nodeEle = (HAPElementStructureNode)contextDefEle;
			JSONObject out = new JSONObject();
			for(String childName : nodeEle.getChildren().keySet()) {
				Object childJsonValue = buildJsonValue(nodeEle.getChild(childName));
				if(childJsonValue!=null) {
					out.put(childName, childJsonValue);
				}
			}
			return out;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
		{
			HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)contextDefEle;
			return new JSONObject();
		}
		default:
			return null;
		}
	}
}
