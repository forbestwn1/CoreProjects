package com.nosliw.core.application.common.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;

public class HAPUtilityValueStructureParser {

	static public void parseValueStructureWrapper(HAPWrapperValueStructure valueStructureWrapper, JSONObject wrapperObj) {

		String groupName = (String)wrapperObj.opt(HAPWrapperValueStructure.NAME);
		valueStructureWrapper.setName(groupName);

		String groupType = (String)wrapperObj.opt(HAPWrapperValueStructure.GROUPTYPE);
		if(groupType==null) {
			groupType = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		}
		valueStructureWrapper.setGroupType(groupType);

		JSONObject infoJsonObj = wrapperObj.optJSONObject(HAPWrapperValueStructure.INFO);
		if(infoJsonObj!=null) {
			HAPInfoImpSimple info = new HAPInfoImpSimple();
			info.buildObject(infoJsonObj, HAPSerializationFormat.JSON);
			valueStructureWrapper.setInfo(info);
		}
	}
	
	static public void parseValueStructureJson(JSONObject structureJson, HAPValueStructureDefinition valueStructure) {
		if(structureJson!=null) {
			Object rootsObj = structureJson.opt(HAPValueStructureDefinition.ROOT);
			if(rootsObj==null) {
				rootsObj = structureJson;
			}
			else {
				valueStructure.setInitValue(structureJson.opt(HAPValueStructureDefinition.INITVALUE));
			}
			List<HAPRootInValueStructure> roots = parseStructureRoots(rootsObj);
			for(HAPRootInValueStructure root : roots) {
				valueStructure.addRoot(root);
			}
		}
	}
	
	static private List<HAPRootInValueStructure> parseStructureRoots(Object rootsObj){
		List<HAPRootInValueStructure> out = new ArrayList<HAPRootInValueStructure>();
		if(rootsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)rootsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPRootInValueStructure root = parseStructureRootFromJson(eleDefJson);
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
				HAPRootInValueStructure root = parseStructureRootFromJson(eleDefJson);
				out.add(root);
			}
		}
		return out;
	}
	
	//parse context root
	static private HAPRootInValueStructure parseStructureRootFromJson(JSONObject eleDefJson){
		HAPRootInValueStructure out = new HAPRootInValueStructure();

		//info
		out.buildEntityInfoByJson(eleDefJson);
		if(!HAPUtilityEntityInfo.isEnabled(out)) {
			return null;
		}

		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPRootInValueStructure.DEFINITION);
		if(defJsonObj!=null) {
			out.setDefinition(HAPParserStructure.parseStructureElement(defJsonObj));
		} else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPElementStructureLeafData());
		}
		return out;
	}
}
