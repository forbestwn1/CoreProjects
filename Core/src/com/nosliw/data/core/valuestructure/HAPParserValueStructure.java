package com.nosliw.data.core.valuestructure;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPParserStructure;

public class HAPParserValueStructure {

	public static HAPValueStructure parseValueStructure(JSONObject jsonObj) {
		return parseValueStructure(jsonObj, null);
	}
	
	public static HAPValueStructure parseValueStructure(JSONObject jsonObj, String typeIfEmpty) {
		HAPValueStructure out = new HAPValueStructureDefinitionEmpty();
		if(jsonObj!=null) {
			String type = jsonObj.optString(HAPValueStructure.TYPE);
			if(HAPUtilityBasic.isStringEmpty(type)) {
				if(jsonObj.optJSONObject(HAPValueStructureDefinitionGroup.GROUP)!=null) {
					type = HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP;
				}
				else if(jsonObj.optJSONObject(HAPValueStructureDefinitionFlat.FLAT)!=null) {
					type = HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT;
				}
			}
			if(type.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
				out = parseValueStructureDefinitionGroup(jsonObj);
			}
			else if(type.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
				out = parseValueStructureDefinitionFlat(jsonObj);
			}
		}
		
		if(out.getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_VALUEEMPTY)&&typeIfEmpty!=null) {
			out = HAPUtilityValueStructure.newValueStructure(typeIfEmpty);
		}
		return out;
	}
	
	public static HAPValueStructureDefinitionGroup parseValueStructureDefinitionGroup(JSONObject groupStructureJson) {
		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
		parseValueStructureDefinitionGroup(groupStructureJson, out);
		return out;
	}
	
	//parse context group
	public static void parseValueStructureDefinitionGroup(JSONObject groupStructureJson, HAPValueStructureDefinitionGroup groupStructure) {
		JSONObject groupJson = groupStructureJson.optJSONObject(HAPValueStructureDefinitionGroup.GROUP);
		if(groupJson!=null) {
			for(String categary : HAPValueStructureDefinitionGroup.getAllCategaries()){
				JSONObject flatStructureJson = groupJson.optJSONObject(categary);
				List<HAPRootStructure> roots = parseRoots(flatStructureJson, new HAPUpdateName() {
					@Override
					public String getUpdatedName(String name) {
						return new HAPReferenceRootInGroup(categary, name).getFullName();
					}
				});
				for(HAPRootStructure root : roots)   groupStructure.addRootToCategary(categary, root);
			}
		}
		groupStructure.getInfo().buildObject(groupStructureJson.opt(HAPValueStructure.INFO), HAPSerializationFormat.JSON);
	}

	public static HAPValueStructureDefinitionFlat parseValueStructureDefinitionFlat(JSONObject structureJson) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		parseValueStructureDefinitionFlat(structureJson, out);
		return out;
	}

	public static void parseValueStructureDefinitionFlat(JSONObject structureJson, HAPValueStructureDefinitionFlat flatStructure) {
		List<HAPRootStructure> roots = parseRoots(structureJson, null);
		for(HAPRootStructure root : roots)  flatStructure.addRoot(root);
		flatStructure.getInfo().buildObject(structureJson.opt(HAPValueStructure.INFO), HAPSerializationFormat.JSON);
	}
	
	private static List<HAPRootStructure> parseRoots(JSONObject structureJson, HAPUpdateName createIdByName) {
		List<HAPRootStructure> out = new ArrayList<HAPRootStructure>();
		if(structureJson!=null) {
			Object elementsObj = structureJson.opt(HAPValueStructureDefinitionFlat.FLAT);
			if(elementsObj==null)  elementsObj = structureJson;
			out = HAPParserStructure.parseStructureRoots(elementsObj);
			for(HAPRootStructure root : out) {
				root.setLocalId(getElementId(root.getLocalId(), root.getName(), createIdByName));
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
