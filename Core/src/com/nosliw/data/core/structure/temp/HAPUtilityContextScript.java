package com.nosliw.data.core.structure.temp;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPElementStructureNode;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPUtilityContextScript {

	public static HAPJsonTypeScript buildValueStructureInitScript(HAPValueStructureDefinitionGroup valueStructure) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		//build init output object 
		JSONObject output = HAPUtilityContextScript.buildDefaultJsonObject(valueStructure);
		templateParms.put("outputInit", HAPJsonUtility.formatJson(output.toString()));

		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityContextScript.class, "ContextInitFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPJsonTypeScript(script);
	}
	
	//build default value structure for context group
	public static JSONObject buildDefaultJsonObject(HAPValueStructureDefinitionGroup contextGroup) {
		JSONObject out = new JSONObject();
		for(String categary : contextGroup.getCategaries()) {
			out.put(categary, buildDefaultJsonObject(contextGroup.getFlat(categary)));
		}
		return out;
	}
	
	public static JSONObject buildDefaultJsonObject(HAPValueStructureDefinitionFlat context) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		for(String contextEleName : context.getRootNames()) {
			Object value = context.getRoot(contextEleName).getDefaultValue();
			if(value!=null) {
				jsonMap.put(contextEleName, value.toString());
			}
		}
		return new JSONObject(HAPJsonUtility.buildMapJson(jsonMap));
	}

	//build skeleton, it is used for data mapping operation
	public static JSONObject buildSkeletonJsonObject(HAPValueStructureDefinitionFlat context, boolean isFlatRootName) {
		JSONObject output = new JSONObject();
		for(String rootName : context.getRootNames()) {
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(context.getRoot(rootName).getInfo()))) {
				HAPElementStructure contextDefEle = context.getRoot(rootName).getDefinition();
				Object contextEleJson = buildJsonValue(contextDefEle);

				if(contextEleJson!=null) {
					JSONObject parentJsonObj = output;
					if(isFlatRootName) {
						//if flat root name, just use it
						parentJsonObj.put(rootName, contextEleJson);
					}
					else {
						//not flat, parse categary and name from root name
//						HAPIdContextDefinitionRoot rootId = new HAPIdContextDefinitionRoot(rootName);
						String categary = rootId.getCategary();
						if(HAPBasicUtility.isStringNotEmpty(categary)) {
							//ignore those that don't have categary
							parentJsonObj = output.optJSONObject(categary);
							if(parentJsonObj==null) {
								parentJsonObj = new JSONObject();
								output.put(categary, parentJsonObj);
							}
						}
						parentJsonObj.put(rootId.getName(), contextEleJson);
					}
				}
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
