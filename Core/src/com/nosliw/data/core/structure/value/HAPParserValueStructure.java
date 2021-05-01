package com.nosliw.data.core.structure.value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPParserStructure;
import com.nosliw.data.core.structure.HAPRoot;

public class HAPParserValueStructure {

	public static HAPStructureValueDefinition parseValueStructureDefinition(JSONObject jsonObj) {
		HAPStructureValueDefinition out = null;
		String type = jsonObj.optString(HAPStructureValueDefinition.TYPE);
		if(HAPBasicUtility.isStringEmpty(type)) {
			if(jsonObj.optJSONObject(HAPStructureValueDefinitionGroup.GROUP)!=null) {
				type = HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT;
			}
			else if(jsonObj.optJSONObject(HAPStructureValueDefinitionFlat.FLAT)!=null) {
				type = HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT;
			}
		}
		if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			out = parseValueStructureDefinitionGroup(jsonObj);
		}
		else if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
			out = parseValueStructureDefinitionFlat(jsonObj);
		}
		return out;
	}
	
	public static HAPStructureValueDefinitionGroup parseValueStructureDefinitionGroup(JSONObject groupStructureJson) {
		if(groupStructureJson==null)  return null;
		HAPStructureValueDefinitionGroup out = new HAPStructureValueDefinitionGroup();
		parseValueStructureDefinitionGroup(groupStructureJson, out);
		return out;
	}
	
	//parse context group
	public static void parseValueStructureDefinitionGroup(JSONObject groupStructureJson, HAPStructureValueDefinitionGroup groupStructure) {
		JSONObject groupJson = groupStructureJson.optJSONObject(HAPStructureValueDefinitionGroup.GROUP);
		if(groupJson!=null) {
			for(String categary : HAPStructureValueDefinitionGroup.getAllCategaries()){
				JSONObject flatStructureJson = groupJson.optJSONObject(categary);
				List<HAPRoot> roots = parseRoots(flatStructureJson, new HAPUpdateName() {
					@Override
					public String getUpdatedName(String name) {
						return new HAPReferenceRootInGroup(categary, name).getFullName();
					}
				});
				for(HAPRoot root : roots)   groupStructure.addRoot(categary, root);
			}
		}
		groupStructure.getInfo().buildObject(groupStructureJson.opt(HAPStructureValueDefinitionGroup.INFO), HAPSerializationFormat.JSON);
	}

	public static HAPStructureValueDefinitionFlat parseValueStructureDefinitionFlat(JSONObject structureJson) {
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
		parseValueStructureDefinitionFlat(structureJson, out);
		return out;
	}

	public static void parseValueStructureDefinitionFlat(JSONObject structureJson, HAPStructureValueDefinitionFlat flatStructure) {
		List<HAPRoot> roots = parseRoots(structureJson, null);
		for(HAPRoot root : roots)  flatStructure.addRoot(root);
	}
	
	private static List<HAPRoot> parseRoots(JSONObject structureJson, HAPUpdateName createIdByName) {
		List<HAPRoot> out = new ArrayList<HAPRoot>();
		if(structureJson!=null) {
			Object elementsObj = structureJson.opt(HAPStructureValueDefinitionFlat.FLAT);
			if(elementsObj==null)  elementsObj = structureJson;
			if(elementsObj instanceof JSONObject) {
				JSONObject elementsJson = (JSONObject)elementsObj;
				Iterator<String> it = elementsJson.keys();
				while(it.hasNext()){
					String eleName = it.next();
					JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
					HAPRoot root = HAPParserStructure.parseContextRootFromJson(eleDefJson);
					root.setName(eleName);
					root.setLocalId(getElementId(root.getLocalId(), root.getName(), createIdByName));
					out.add(root);
				}
			}
			else if(elementsObj instanceof JSONArray) {
				JSONArray elementsArray = (JSONArray)elementsObj;
				for(int i=0; i<elementsArray.length(); i++) {
					JSONObject eleDefJson = elementsArray.getJSONObject(i);
					HAPRoot root = HAPParserStructure.parseContextRootFromJson(eleDefJson);
					root.setLocalId(getElementId(root.getLocalId(), root.getName(), createIdByName));
					out.add(root);
				}
			}
		}
		return out;
	}
	
	private static String getElementId(String localId, String name, HAPUpdateName createIdByName) {
		if(localId!=null)  return localId;
		if(createIdByName!=null)   return createIdByName.getUpdatedName(name);
		return name;
	}

}
