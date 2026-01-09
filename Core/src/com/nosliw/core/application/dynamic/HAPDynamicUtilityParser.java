package com.nosliw.core.application.dynamic;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.brickcriteria.HAPCriteriaBrick;
import com.nosliw.core.application.entity.brickcriteria.HAPManagerBrickCriteria;

public class HAPDynamicUtilityParser {

	public static HAPDynamicDefinitionContainer parseDynamicDefinitionContainer(Object json, HAPDynamicDefinitionContainer dynamicInfo, HAPManagerBrickCriteria brickCriteriaMan) {
		if(json instanceof JSONArray) {
			parseDynamicDefinitionItems(dynamicInfo, (JSONArray)json, brickCriteriaMan);
		}
		else if(json instanceof JSONObject) {
			parseDynamicDefinitionItems(dynamicInfo, ((JSONObject)json).optJSONArray(HAPDynamicDefinitionContainer.ELEMENT), brickCriteriaMan);
		}
		return dynamicInfo;
	}

	private static void parseDynamicDefinitionItems(HAPDynamicDefinitionContainer out, JSONArray jsonArray, HAPManagerBrickCriteria brickCriteriaMan) {
		if(jsonArray!=null) {
			for(int i=0; i<jsonArray.length(); i++) {
				out.addElement(parseDynamicDefinitionItem(jsonArray.get(i), brickCriteriaMan));
			}
		}
	}

	private static HAPDynamicDefinitionItem parseDynamicDefinitionItem(Object obj, HAPManagerBrickCriteria brickCriteriaMan) {
		HAPDynamicDefinitionItem out = null;
		
		JSONObject jsonObj = (JSONObject)obj;
		String type = jsonObj.getString(HAPDynamicDefinitionItem.TYPE);
		
		switch(type) {
		case HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_SET:
			out = new HAPDynamicDefinitionItemSet();
			parseToDynamicDefinitionItemLeaf((HAPDynamicDefinitionItemSet)out, jsonObj, brickCriteriaMan);
			break;
		case HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_SINGLE:
			out = new HAPDynamicDefinitionItemSingle();
			parseToDynamicDefinitionItemLeaf((HAPDynamicDefinitionItemSingle)out, jsonObj, brickCriteriaMan);
			break;
		case HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_NODE:
			out = new HAPDynamicDefinitionItemNode();
		    parseToDynamicDefinitionItem(out, jsonObj);
			
			JSONArray childArray = jsonObj.getJSONArray(HAPDynamicDefinitionItemNode.CHILD);
			for(int i=0; i<childArray.length(); i++) {
				((HAPDynamicDefinitionItemNode)out).addChild(parseDynamicDefinitionItem(childArray.getJSONObject(i), brickCriteriaMan));
			}
			break;
		}
		
		if(out.getName()==null) {
			out.setName(HAPConstantShared.NAME_DEFAULT);
		}
		
		return out;
	}
	
	private static void parseToDynamicDefinitionItemLeaf(HAPDynamicDefinitionItemLeaf leafItem, JSONObject jsonObj, HAPManagerBrickCriteria brickCriteriaMan) {
	    parseToDynamicDefinitionItem(leafItem, jsonObj);
		leafItem.setCriteria(parseDynamicCriteria(jsonObj.getJSONObject(HAPDynamicDefinitionItemLeaf.CRITERIA), brickCriteriaMan));
	}

    private static void parseToDynamicDefinitionItem(HAPDynamicDefinitionItem item, JSONObject jsonObj) {
		item.buildEntityInfoByJson(jsonObj);
	}

	private static HAPDynamicDefinitionCriteria parseDynamicCriteria(JSONObject jsonObj, HAPManagerBrickCriteria brickCriteriaMan) {
		
		String type = (String)jsonObj.opt(HAPDynamicDefinitionCriteria.TYPE);
		if(type==null) {
			type = HAPConstantShared.DYNAMICDEFINITION_CRITERIA_SIMPLE;
		}
		
		switch(type) {
		case HAPConstantShared.DYNAMICDEFINITION_CRITERIA_SIMPLE:
			HAPDynamicDefinitionCriteriaSimple out = new HAPDynamicDefinitionCriteriaSimple();
			Object criteriaObj = jsonObj.opt(HAPDynamicDefinitionCriteriaSimple.CRITERIA);
			if(criteriaObj==null) {
				criteriaObj = jsonObj;
			}
			HAPCriteriaBrick brickCriteria = brickCriteriaMan.parseCriteria(criteriaObj);
			out.setBrickCriteria(brickCriteria);
			return out;
		}
		return null;
	}

}
