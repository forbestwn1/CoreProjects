package com.nosliw.data.core.structure.value;

import java.util.Iterator;

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
	
	public static HAPStructureValueDefinitionGroup parseValueStructureDefinitionGroup(JSONObject contextGroupJson) {
		if(contextGroupJson==null)  return null;
		HAPStructureValueDefinitionGroup out = new HAPStructureValueDefinitionGroup();
		parseValueStructureDefinitionGroup(contextGroupJson, out);
		return out;
	}
	
	//parse context group
	public static void parseValueStructureDefinitionGroup(JSONObject contextGroupJson, HAPStructureValueDefinitionGroup contextGroup) {
		JSONObject groupJson = contextGroupJson.optJSONObject(HAPStructureValueDefinitionGroup.GROUP);
		if(groupJson!=null) {
			for(String contextType : HAPStructureValueDefinitionGroup.getAllContextTypes()){
				JSONObject contextEleJson = groupJson.optJSONObject(contextType);
				HAPStructureValueDefinitionFlat context = contextGroup.getContext(contextType);
				parseparseValueStructureDefinitionFlat(contextEleJson, context, new HAPUpdateName() {
					@Override
					public String getUpdatedName(String name) {
						return new HAPReferenceRootInGroup(contextType, name).getFullName();
					}
					
				});
			}
		}
		contextGroup.getInfo().buildObject(contextGroupJson.opt(HAPStructureValueDefinitionGroup.INFO), HAPSerializationFormat.JSON);
	}

	public static HAPStructureValueDefinitionFlat parseValueStructureDefinitionFlat(JSONObject contextJson) {
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
		parseparseValueStructureDefinitionFlat(contextJson, out);
		return out;
	}

	public static void parseparseValueStructureDefinitionFlat(JSONObject contextJson, HAPStructureValueDefinitionFlat context) {
		parseparseValueStructureDefinitionFlat(contextJson, context, null);
	}
		
	private static void parseparseValueStructureDefinitionFlat(JSONObject contextJson, HAPStructureValueDefinitionFlat context, HAPUpdateName createIdByName) {
		if(contextJson!=null) {
			Object elementsObj = contextJson.opt(HAPStructureValueDefinitionFlat.FLAT);
			if(elementsObj==null)  elementsObj = contextJson;
			if(elementsObj instanceof JSONObject) {
				JSONObject elementsJson = (JSONObject)elementsObj;
				Iterator<String> it = elementsJson.keys();
				while(it.hasNext()){
					String eleName = it.next();
					JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
					HAPRoot contextDefRoot = HAPParserStructure.parseContextRootFromJson(eleDefJson);
					context.addRoot(eleName, getElementId(contextDefRoot.getLocalId(), contextDefRoot.getName(), createIdByName), contextDefRoot);
				}
			}
			else if(elementsObj instanceof JSONArray) {
				JSONArray elementsArray = (JSONArray)elementsObj;
				for(int i=0; i<elementsArray.length(); i++) {
					JSONObject eleDefJson = elementsArray.getJSONObject(i);
					HAPRoot contextDefRoot = HAPParserStructure.parseContextRootFromJson(eleDefJson);
					context.addRoot(contextDefRoot.getName(), getElementId(contextDefRoot.getLocalId(), contextDefRoot.getName(), createIdByName),contextDefRoot);
				}
			}
		}
	}
	
	private static String getElementId(String localId, String name, HAPUpdateName createIdByName) {
		if(localId!=null)  return localId;
		if(createIdByName!=null)   return createIdByName.getUpdatedName(name);
		return name;
	}

}
