package com.nosliw.data.core.story;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.element.connection.HAPConnectionContain;
import com.nosliw.data.core.story.element.connectiongroup.HAPElementGroupSwitch;
import com.nosliw.data.core.story.element.node.HAPStoryNodeService;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceInput;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceOutput;

public class HAPParserElement {

	private static final Map<String, Class<? extends HAPStoryNode>> m_storyNodeClass = new LinkedHashMap<String, Class<? extends HAPStoryNode>>();
	private static final Map<String, Class<? extends HAPConnection>> m_storyConnectionClass = new LinkedHashMap<String, Class<? extends HAPConnection>>();
	private static final Map<String, Class<? extends HAPElementGroup>> m_storyGroupClass = new LinkedHashMap<String, Class<? extends HAPElementGroup>>();
	
	static {
		m_storyNodeClass.put(HAPStoryNodeService.STORYNODE_TYPE, HAPStoryNodeService.class);
		m_storyNodeClass.put(HAPStoryNodeServiceInput.STORYNODE_TYPE, HAPStoryNodeServiceInput.class);
		m_storyNodeClass.put(HAPStoryNodeServiceOutput.STORYNODE_TYPE, HAPStoryNodeServiceOutput.class);
		
		m_storyConnectionClass.put(HAPConnectionContain.CONNECTION_TYPE, HAPConnectionContain.class);
	
		m_storyGroupClass.put(HAPElementGroupSwitch.GROUP_TYPE, HAPElementGroupSwitch.class);
	}
	
	public static HAPStoryElement parseElement(JSONObject jsonObj, HAPStory story) {
		HAPStoryElement out = null;
		String categary = jsonObj.getString(HAPStoryElement.CATEGARY);
		if(categary.equals(HAPConstant.STORYELEMENT_CATEGARY_NODE))   out = parseNode(jsonObj);
		else if(categary.equals(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION))   out = parseConnection(jsonObj);
		else if(categary.equals(HAPConstant.STORYELEMENT_CATEGARY_GROUP))   out = parseElementGroup(jsonObj, story);
		return out;
	}
	
	public static HAPStoryNode parseNode(JSONObject jsonObj) {
		HAPStoryNode out = null;
		try {
			String type = jsonObj.getString(HAPStoryElement.TYPE);
			out = m_storyNodeClass.get(type).newInstance();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public static HAPConnection parseConnection(JSONObject jsonObj) {
		HAPConnection out = null;
		try {
			String type = jsonObj.getString(HAPStoryElement.TYPE);
			out = m_storyConnectionClass.get(type).newInstance();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static HAPElementGroup parseElementGroup(JSONObject jsonObj, HAPStory story) {
		HAPElementGroup out = null;
		try {
			String type = jsonObj.getString(HAPStoryElement.TYPE);
			out = m_storyGroupClass.get(type).getConstructor(HAPStory.class).newInstance(story);
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return out;
	}

}
