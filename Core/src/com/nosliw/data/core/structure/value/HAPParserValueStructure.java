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

	public static HAPContextStructureValueDefinition parseValueStructureDefinition(JSONObject jsonObj) {
		HAPContextStructureValueDefinition out = null;
		String type = jsonObj.optString(HAPContextStructureValueDefinition.TYPE);
		if(HAPBasicUtility.isStringEmpty(type)) {
			if(jsonObj.optJSONObject(HAPContextStructureValueDefinitionGroup.GROUP)!=null) {
				type = HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT;
			}
			else if(jsonObj.optJSONObject(HAPContextStructureValueDefinitionFlat.ELEMENT)!=null) {
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
	
	public static HAPContextStructureValueDefinitionGroup parseValueStructureDefinitionGroup(JSONObject contextGroupJson) {
		if(contextGroupJson==null)  return null;
		HAPContextStructureValueDefinitionGroup out = new HAPContextStructureValueDefinitionGroup();
		parseValueStructureDefinitionGroup(contextGroupJson, out);
		return out;
	}
	
	//parse context group
	public static void parseValueStructureDefinitionGroup(JSONObject contextGroupJson, HAPContextStructureValueDefinitionGroup contextGroup) {
		JSONObject groupJson = contextGroupJson.optJSONObject(HAPContextStructureValueDefinitionGroup.GROUP);
		if(groupJson!=null) {
			for(String contextType : HAPContextStructureValueDefinitionGroup.getAllContextTypes()){
				JSONObject contextEleJson = groupJson.optJSONObject(contextType);
				HAPContextStructureValueDefinitionFlat context = contextGroup.getContext(contextType);
				parseparseValueStructureDefinitionFlat(contextEleJson, context, new HAPUpdateName() {
					@Override
					public String getUpdatedName(String name) {
						return new HAPIdRootInGroup(contextType, name).getFullName();
					}
					
				});
			}
		}
		contextGroup.getInfo().buildObject(contextGroupJson.opt(HAPContextStructureValueDefinitionGroup.INFO), HAPSerializationFormat.JSON);
	}

	public static HAPContextStructureValueDefinitionFlat parseValueStructureDefinitionFlat(JSONObject contextJson) {
		HAPContextStructureValueDefinitionFlat out = new HAPContextStructureValueDefinitionFlat();
		parseparseValueStructureDefinitionFlat(contextJson, out);
		return out;
	}

	public static void parseparseValueStructureDefinitionFlat(JSONObject contextJson, HAPContextStructureValueDefinitionFlat context) {
		parseparseValueStructureDefinitionFlat(contextJson, context, null);
	}
		
	private static void parseparseValueStructureDefinitionFlat(JSONObject contextJson, HAPContextStructureValueDefinitionFlat context, HAPUpdateName createIdByName) {
		if(contextJson!=null) {
			Object elementsObj = contextJson.opt(HAPContextStructureValueDefinitionFlat.ELEMENT);
			if(elementsObj==null)  elementsObj = contextJson;
			if(elementsObj instanceof JSONObject) {
				JSONObject elementsJson = (JSONObject)elementsObj;
				Iterator<String> it = elementsJson.keys();
				while(it.hasNext()){
					String eleName = it.next();
					JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
					HAPRoot contextDefRoot = HAPParserStructure.parseContextRootFromJson(eleDefJson);
					context.addElement(eleName, getElementId(contextDefRoot.getLocalId(), contextDefRoot.getName(), createIdByName), contextDefRoot);
				}
			}
			else if(elementsObj instanceof JSONArray) {
				JSONArray elementsArray = (JSONArray)elementsObj;
				for(int i=0; i<elementsArray.length(); i++) {
					JSONObject eleDefJson = elementsArray.getJSONObject(i);
					HAPRoot contextDefRoot = HAPParserStructure.parseContextRootFromJson(eleDefJson);
					context.addElement(contextDefRoot.getName(), getElementId(contextDefRoot.getLocalId(), contextDefRoot.getName(), createIdByName),contextDefRoot);
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
