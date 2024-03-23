package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPParserStructure;
import com.nosliw.core.application.valuestructure.HAPRootStructure;

public class HAPParserValueStructure {

	public static List<HAPRootStructure> parseStructureRoots(Object rootsObj){
		List<HAPRootStructure> out = new ArrayList<HAPRootStructure>();
		if(rootsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)rootsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPRootStructure root = parseStructureRootFromJson(eleDefJson);
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
				HAPRootStructure root = parseStructureRootFromJson(eleDefJson);
				out.add(root);
			}
		}
		return out;
	}
	
	//parse context root
	public static HAPRootStructure parseStructureRootFromJson(JSONObject eleDefJson){
		HAPRootStructure out = new HAPRootStructure();

		//info
		out.buildEntityInfoByJson(eleDefJson);
		if(!HAPUtilityEntityInfo.isEnabled(out))   return null;

		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPRootStructure.DEFINITION);
		if(defJsonObj!=null)  out.setDefinition(HAPParserStructure.parseStructureElement(defJsonObj));
		else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPElementStructureLeafData());
		}
		Object defaultJsonObj = eleDefJson.opt(HAPRootStructure.DEFAULT);
		out.setDefaultValue(defaultJsonObj);
		return out;
	}
	
}
