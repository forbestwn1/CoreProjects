package com.nosliw.core.application.division.story;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryConnection;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryElementGroup;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.brick.connection.HAPStoryConnectionContain;
import com.nosliw.core.application.division.story.brick.connection.HAPStoryConnectionDataIO;
import com.nosliw.core.application.division.story.brick.group.HAPStoryElementGroupBatch;
import com.nosliw.core.application.division.story.brick.group.HAPStoryElementGroupSwitch;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeConstant;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeModule;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeScript;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeService;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeServiceInput;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeServiceInputParm;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeServiceOutput;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeServiceOutputItem;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUIHtml;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUIPage;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUITagData;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUITagOther;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeVariable;

public class HAPStoryParserElement {

	private static final Map<String, Class<? extends HAPStoryNode>> m_storyNodeClass = new LinkedHashMap<String, Class<? extends HAPStoryNode>>();
	private static final Map<String, Class<? extends HAPStoryConnection>> m_storyConnectionClass = new LinkedHashMap<String, Class<? extends HAPStoryConnection>>();
	private static final Map<String, Class<? extends HAPStoryElementGroup>> m_storyGroupClass = new LinkedHashMap<String, Class<? extends HAPStoryElementGroup>>();
	
	static {
		m_storyNodeClass.put(HAPStoryNodeModule.STORYNODE_TYPE, HAPStoryNodeModule.class);

		m_storyNodeClass.put(HAPStoryNodeScript.STORYNODE_TYPE, HAPStoryNodeScript.class);
		m_storyNodeClass.put(HAPStoryNodeService.STORYNODE_TYPE, HAPStoryNodeService.class);
		m_storyNodeClass.put(HAPStoryNodeServiceInput.STORYNODE_TYPE, HAPStoryNodeServiceInput.class);
		m_storyNodeClass.put(HAPStoryNodeServiceInputParm.STORYNODE_TYPE, HAPStoryNodeServiceInputParm.class);
		m_storyNodeClass.put(HAPStoryNodeServiceOutput.STORYNODE_TYPE, HAPStoryNodeServiceOutput.class);
		m_storyNodeClass.put(HAPStoryNodeServiceOutputItem.STORYNODE_TYPE, HAPStoryNodeServiceOutputItem.class);
		m_storyNodeClass.put(HAPStoryNodeConstant.STORYNODE_TYPE, HAPStoryNodeConstant.class);
		m_storyNodeClass.put(HAPStoryNodeVariable.STORYNODE_TYPE, HAPStoryNodeVariable.class);

		m_storyNodeClass.put(HAPStoryNodeUIPage.STORYNODE_TYPE, HAPStoryNodeUIPage.class);
		m_storyNodeClass.put(HAPStoryNodeUIHtml.STORYNODE_TYPE, HAPStoryNodeUIHtml.class);
		m_storyNodeClass.put(HAPStoryNodeUITagData.STORYNODE_TYPE, HAPStoryNodeUITagData.class);
		m_storyNodeClass.put(HAPStoryNodeUITagOther.STORYNODE_TYPE, HAPStoryNodeUITagOther.class);

		
		m_storyConnectionClass.put(HAPStoryConnectionContain.CONNECTION_TYPE, HAPStoryConnectionContain.class);
		m_storyConnectionClass.put(HAPStoryConnectionDataIO.CONNECTION_TYPE, HAPStoryConnectionDataIO.class);

		m_storyGroupClass.put(HAPStoryElementGroupSwitch.GROUP_TYPE, HAPStoryElementGroupSwitch.class);
		m_storyGroupClass.put(HAPStoryElementGroupBatch.GROUP_TYPE, HAPStoryElementGroupBatch.class);
	}
	
	public static void registerStoryNode(String nodeType, Class<? extends HAPStoryNode> node) {	m_storyNodeClass.put(nodeType, node);	}
	public static void registerStoryConnection(String nodeType, Class<HAPStoryConnection> connection) {	m_storyConnectionClass.put(nodeType, connection);	}
	
	public static HAPStoryElement parseElement(JSONObject jsonObj) {
		HAPStoryElement out = null;
		String categary = jsonObj.getString(HAPStoryElement.CATEGARY);
		if(categary.equals(HAPConstantShared.STORYELEMENT_CATEGARY_NODE)) {
			out = parseNode(jsonObj);
		} else if(categary.equals(HAPConstantShared.STORYELEMENT_CATEGARY_CONNECTION)) {
			out = parseConnection(jsonObj);
		} else if(categary.equals(HAPConstantShared.STORYELEMENT_CATEGARY_GROUP)) {
			out = parseElementGroup(jsonObj);
		}
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
	
	public static HAPStoryConnection parseConnection(JSONObject jsonObj) {
		HAPStoryConnection out = null;
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

	public static HAPStoryElementGroup parseElementGroup(JSONObject jsonObj) {
		HAPStoryElementGroup out = null;
		try {
			String type = jsonObj.getString(HAPStoryElement.TYPE);
			out = m_storyGroupClass.get(type).getConstructor().newInstance();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return out;
	}

}
