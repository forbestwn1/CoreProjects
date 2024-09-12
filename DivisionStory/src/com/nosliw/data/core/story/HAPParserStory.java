package com.nosliw.data.core.story;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.story.change.HAPManagerChange;

public class HAPParserStory {

	public static HAPStoryImp parseStory(JSONObject jsonObj, HAPManagerChange changeMan) {
		HAPStoryImp out = new HAPStoryImp(changeMan);
		
		out.buildEntityInfoByJson(jsonObj);
		
		out.setTopicType(jsonObj.getString(HAPStory.SHOWTYPE));
		
		JSONArray nodeJsonArray = jsonObj.optJSONArray(HAPStory.NODE);
		if(nodeJsonArray!=null) {
			for(int i=0; i<nodeJsonArray.length(); i++) {
				JSONObject nodeJsonObj = nodeJsonArray.getJSONObject(i);
				HAPStoryNode storyNode = HAPParserElement.parseNode(nodeJsonObj);
				out.addElement(storyNode, null);
			}
		}
		
		JSONArray connectionJsonArray = jsonObj.optJSONArray(HAPStory.CONNECTION);
		if(connectionJsonArray!=null) {
			for(int i=0; i<connectionJsonArray.length(); i++) {
				JSONObject connectionJsonObj = connectionJsonArray.getJSONObject(i);
				HAPConnection connection = HAPParserElement.parseConnection(connectionJsonObj);
				out.addElement(connection, null);
			}
		}
		
		JSONArray connectionGroupJsonArray = jsonObj.optJSONArray(HAPStory.ELEMENTGROUP);
		if(connectionGroupJsonArray!=null) {
			for(int i=0; i<connectionGroupJsonArray.length(); i++) {
				JSONObject connectionGroupJsonObj = connectionGroupJsonArray.getJSONObject(i);
				HAPElementGroup connectionGroup = HAPParserElement.parseElementGroup(connectionGroupJsonObj);
				out.addElement(connectionGroup, null);
			}
		}

		JSONObject aliasObjects = jsonObj.optJSONObject(HAPStory.ALIAS);
		if(aliasObjects!=null) {
			for(Object key : aliasObjects.keySet()) {
				String aliasName = (String)key;
				JSONObject eleIdObj = aliasObjects.getJSONObject(aliasName);
				HAPIdElement eleId = new HAPIdElement();
				eleId.buildObject(eleIdObj, HAPSerializationFormat.JSON);
				out.setAlias(new HAPAliasElement(aliasName, false), eleId);
			}
		}
		
		return out;
	}
}
