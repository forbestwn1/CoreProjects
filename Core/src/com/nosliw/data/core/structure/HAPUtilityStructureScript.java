package com.nosliw.data.core.structure;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.structure.temp.HAPUtilityContextInfo;
import com.nosliw.data.core.valuestructure.HAPExecutableValueStructure;

public class HAPUtilityStructureScript {


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
