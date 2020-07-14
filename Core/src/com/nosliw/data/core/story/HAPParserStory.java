package com.nosliw.data.core.story;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParserStory {

	public static HAPStoryImp parseStory(JSONObject jsonObj) {
		HAPStoryImp out = new HAPStoryImp();
		
		out.buildEntityInfoByJson(jsonObj);
		
		out.setTopicType(jsonObj.getString(HAPStory.SHOWTYPE));
		
		JSONArray nodeJsonArray = jsonObj.optJSONArray(HAPStory.NODE);
		if(nodeJsonArray!=null) {
			for(int i=0; i<nodeJsonArray.length(); i++) {
				JSONObject nodeJsonObj = nodeJsonArray.getJSONObject(i);
				HAPStoryNode storyNode = parseStoryNode(nodeJsonObj);
				out.addNode(storyNode);
			}
		}
		
		JSONArray connectionJsonArray = jsonObj.optJSONArray(HAPStory.CONNECTION);
		if(connectionJsonArray!=null) {
			for(int i=0; i<connectionJsonArray.length(); i++) {
				JSONObject connectionJsonObj = connectionJsonArray.getJSONObject(i);
				HAPConnection connection = parseConnection(connectionJsonObj);
				
				out.getNode(connection.getEnd1().getNodeId()).addConnection(connection.getId());
				out.getNode(connection.getEnd2().getNodeId()).addConnection(connection.getId());
				
				out.addConnection(connection);
			}
		}
		
		JSONArray connectionGroupJsonArray = jsonObj.optJSONArray(HAPStory.CONNECTIONGROUP);
		if(connectionGroupJsonArray!=null) {
			for(int i=0; i<connectionGroupJsonArray.length(); i++) {
				JSONObject connectionGroupJsonObj = connectionGroupJsonArray.getJSONObject(i);
				HAPConnectionGroup connectionGroup = parseConnectionGroup(connectionGroupJsonObj);
				out.addConnectionGroup(connectionGroup);
			}
		}
		
		return out;
	}
	
	private static HAPStoryNode parseStoryNode(JSONObject jsonObj) {
		HAPStoryNodeImp out = new HAPStoryNodeImp();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	private static HAPConnection parseConnection(JSONObject jsonObj) {
		HAPConnectionImp out = new HAPConnectionImp();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	private static HAPConnectionGroup parseConnectionGroup(JSONObject jsonObj) {
		HAPConnectionGroupImp out = new HAPConnectionGroupImp();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}

}
