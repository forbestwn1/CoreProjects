package com.nosliw.core.application.common.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPUtilityValueStructureParser {

	static public void parseValueStructureWrapper(HAPWrapperValueStructure valueStructureWrapper, JSONObject wrapperObj) {
		HAPUtilityEntityInfo.buildEntityInfoByJson(wrapperObj, valueStructureWrapper);

		String groupType = (String)wrapperObj.opt(HAPWrapperValueStructure.GROUPTYPE);
		if(groupType==null) {
			groupType = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		}
		valueStructureWrapper.setGroupType(groupType);

		String inheritMode = (String)wrapperObj.opt(HAPWrapperValueStructure.INHERITMODE);
		if(inheritMode!=null) {
			valueStructureWrapper.setInheritMode(inheritMode);
		}
	}
	
	static public void parseValueStructureJson(JSONObject structureJson, HAPValueStructure valueStructure) {
		if(structureJson!=null) {
			Object rootsObj = structureJson.opt(HAPStructure.ROOT);
			if(rootsObj==null) {
				rootsObj = structureJson;
			}
			else {
				valueStructure.setInitValue(structureJson.opt(HAPValueStructure.INITVALUE));
			}
			List<HAPRootInStructure> roots = parseStructureRoots(rootsObj);
			for(HAPRootInStructure root : roots) {
				valueStructure.addRoot(root);
			}
		}
	}
	
	static private List<HAPRootInStructure> parseStructureRoots(Object rootsObj){
		List<HAPRootInStructure> out = new ArrayList<HAPRootInStructure>();
		if(rootsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)rootsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPRootInStructure root = parseStructureRootFromJson(eleDefJson);
				if(root!=null) {
					root.setName(eleName);
					out.add(root);
				}
			}
		}
		else if(rootsObj instanceof JSONArray) {
			JSONArray elementsArray = (JSONArray)rootsObj;
			for(int i=0; i<elementsArray.length(); i++) {
				JSONObject eleDefJson = elementsArray.getJSONObject(i);
				HAPRootInStructure root = parseStructureRootFromJson(eleDefJson);
				out.add(root);
			}
		}
		return out;
	}
	
	//parse context root
	static private HAPRootInStructure parseStructureRootFromJson(JSONObject eleDefJson){
		HAPRootInStructure out = new HAPRootInStructure();

		//info
		out.buildEntityInfoByJson(eleDefJson);
		if(!HAPUtilityEntityInfo.isEnabled(out)) {
			return null;
		}

		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPRootInStructure.DEFINITION);
		if(defJsonObj!=null) {
			out.setDefinition(HAPParserStructure.parseStructureElement(defJsonObj));
		} else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPElementStructureLeafData());
		}
		return out;
	}
}
