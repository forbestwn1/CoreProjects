package com.nosliw.data.core.story.resource;

import java.io.File;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.resource.dynamic.HAPParmDefinition;
import com.nosliw.data.core.story.HAPConnection;
import com.nosliw.data.core.story.HAPConnectionGroup;
import com.nosliw.data.core.story.HAPConnectionGroupImp;
import com.nosliw.data.core.story.HAPConnectionImp;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryImp;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.HAPStoryNodeImp;

public class HAPParserStory {

	private HAPManagerStory m_storyMan;
	
	public static HAPResourceDefinitionStory parseFile(String fileName){
		HAPResourceDefinitionStory out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(input);
			String source = HAPFileUtility.readFile(input);
			out = parseContent(source, resourceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private static HAPResourceDefinitionStory parseContent(String content, String id) {
		JSONObject jsonObj = new JSONObject(content);
		HAPResourceDefinitionStory out = parseStoryDefinition(jsonObj);
		out.setId(id);
		return out;
	}

	public static HAPResourceDefinitionStory parseStoryDefinition(JSONObject jsonObj) {
		HAPResourceDefinitionStory out = new HAPResourceDefinitionStory();

		//build complex resource part from json object
		HAPUtilityComponentParse.parseComplextResourceDefinition(out, jsonObj);

		//builder id
		out.setBuilderId(jsonObj.getString(HAPResourceDefinitionStory.BUILDERID));
		
		//parms
		Map<String, HAPAttachment> attachments = out.getAttachmentsByType(HAPConstant.RUNTIME_RESOURCE_TYPE_TESTDATA);
		for(String setName : attachments.keySet()) {
			HAPAttachmentEntity att = (HAPAttachmentEntity)attachments.get(setName);
			JSONArray parmJsonArray = att.getEntityJsonArray();
			for(int i=0; i<parmJsonArray.length(); i++) {
				HAPParmDefinition parmDef = new HAPParmDefinition();
				parmDef.buildObject(parmJsonArray.get(i), HAPSerializationFormat.JSON);
				out.addParmDefinition(setName, parmDef);
			}
		}
		
		return out;
	}
	
	private static HAPStoryImp parseStory(JSONObject jsonObj, HAPManagerStory storyMan) {
		HAPStoryImp out = new HAPStoryImp();
		
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
