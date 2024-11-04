package com.nosliw.core.application.division.story;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.story.brick.HAPStoryConnection;
import com.nosliw.core.application.division.story.brick.HAPStoryElementGroup;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;

public class HAPStoryParserStory {

	public static HAPStoryStoryImp parseStory(JSONObject jsonObj, HAPStoryManagerChange changeMan) {
		HAPStoryStoryImp out = new HAPStoryStoryImp(changeMan);
		
		out.buildEntityInfoByJson(jsonObj);
		
		out.setTopicType(jsonObj.getString(HAPStoryStory.SHOWTYPE));
		
		JSONArray nodeJsonArray = jsonObj.optJSONArray(HAPStoryStory.NODE);
		if(nodeJsonArray!=null) {
			for(int i=0; i<nodeJsonArray.length(); i++) {
				JSONObject nodeJsonObj = nodeJsonArray.getJSONObject(i);
				HAPStoryNode storyNode = HAPStoryParserElement.parseNode(nodeJsonObj);
				out.addElement(storyNode, null);
			}
		}
		
		JSONArray connectionJsonArray = jsonObj.optJSONArray(HAPStoryStory.CONNECTION);
		if(connectionJsonArray!=null) {
			for(int i=0; i<connectionJsonArray.length(); i++) {
				JSONObject connectionJsonObj = connectionJsonArray.getJSONObject(i);
				HAPStoryConnection connection = HAPStoryParserElement.parseConnection(connectionJsonObj);
				out.addElement(connection, null);
			}
		}
		
		JSONArray connectionGroupJsonArray = jsonObj.optJSONArray(HAPStoryStory.ELEMENTGROUP);
		if(connectionGroupJsonArray!=null) {
			for(int i=0; i<connectionGroupJsonArray.length(); i++) {
				JSONObject connectionGroupJsonObj = connectionGroupJsonArray.getJSONObject(i);
				HAPStoryElementGroup connectionGroup = HAPStoryParserElement.parseElementGroup(connectionGroupJsonObj);
				out.addElement(connectionGroup, null);
			}
		}

		JSONObject aliasObjects = jsonObj.optJSONObject(HAPStoryStory.ALIAS);
		if(aliasObjects!=null) {
			for(Object key : aliasObjects.keySet()) {
				String aliasName = (String)key;
				JSONObject eleIdObj = aliasObjects.getJSONObject(aliasName);
				HAPStoryIdElement eleId = new HAPStoryIdElement();
				eleId.buildObject(eleIdObj, HAPSerializationFormat.JSON);
				out.setAlias(new HAPStoryAliasElement(aliasName, false), eleId);
			}
		}
		
		return out;
	}
}
