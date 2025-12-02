package com.nosliw.core.application.common.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPUtilityParserStructure {

	static public void parseValueStructureWrapper(HAPWrapperValueStructureDefinition valueStructureWrapper, JSONObject wrapperObj) {
		HAPUtilityEntityInfo.buildEntityInfoByJson(wrapperObj, valueStructureWrapper);

		HAPInfoStructureInWrapper structureInfo = new HAPInfoStructureInWrapper();
		
		String scope = (String)wrapperObj.opt(HAPInfoStructureInWrapper.SCOPE);
		if(scope==null) {
			scope = HAPUtilityScope.DEFAULT_SCOPE;
		}
		structureInfo.setScope(scope);

		String inheritMode = (String)wrapperObj.opt(HAPInfoStructureInWrapper.INHERITMODE);
		if(inheritMode!=null) {
			structureInfo.setInheritMode(inheritMode);
		}
		valueStructureWrapper.setStructureInfo(structureInfo);
	}
	
	static public void parseValueStructureJson(JSONObject structureJson, HAPValueStructure valueStructure, HAPManagerDataRule dataRuleMan) {
		if(structureJson!=null) {
			Object rootsObj = structureJson.opt(HAPStructure.ROOT);
			if(rootsObj==null) {
				rootsObj = structureJson;
			}
			else {
				valueStructure.setInitValue(structureJson.opt(HAPValueStructure.INITVALUE));
			}
			List<HAPRootInStructure> roots = parseStructureRoots(rootsObj, dataRuleMan);
			for(HAPRootInStructure root : roots) {
				valueStructure.addRoot(root);
			}
		}
	}
	
	static private List<HAPRootInStructure> parseStructureRoots(Object rootsObj, HAPManagerDataRule dataRuleMan){
		List<HAPRootInStructure> out = new ArrayList<HAPRootInStructure>();
		if(rootsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)rootsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPRootInStructure root = parseStructureRootFromJson(eleDefJson, dataRuleMan);
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
				HAPRootInStructure root = parseStructureRootFromJson(eleDefJson, dataRuleMan);
				out.add(root);
			}
		}
		return out;
	}
	
	//parse context root
	static private HAPRootInStructure parseStructureRootFromJson(JSONObject eleDefJson, HAPManagerDataRule dataRuleMan){
		HAPRootInStructure out = new HAPRootInStructure();

		//info
		out.buildEntityInfoByJson(eleDefJson);
		if(!HAPUtilityEntityInfo.isEnabled(out)) {
			return null;
		}

		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPRootInStructure.DEFINITION);
		if(defJsonObj!=null) {
			out.setDefinition(HAPUtilityParserElement.parseStructureElement(defJsonObj, dataRuleMan));
		} else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPElementStructureLeafData());
		}
		return out;
	}
}
