package com.nosliw.core.application.division.story.brick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public abstract class HAPStoryNodeImp extends HAPStoryElementImp implements HAPStoryNode{

	private List<String> m_connections;
	
	public HAPStoryNodeImp() {
		super(HAPConstantShared.STORYELEMENT_CATEGARY_NODE);
		this.m_connections = new ArrayList<String>();
	}

	public HAPStoryNodeImp(String type) {
		super(HAPConstantShared.STORYELEMENT_CATEGARY_NODE, type);
		this.m_connections = new ArrayList<String>();
	}

	@Override
	public List<String> getConnections() {  return this.m_connections;  }
 
	@Override
	public void addConnection(String connectionId) {  this.m_connections.add(connectionId);   }
	
	protected void cloneTo(HAPStoryNodeImp storyNode) {
		super.cloneTo(storyNode);
		storyNode.m_connections.addAll(this.m_connections);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONNECTIONS, HAPUtilityJson.buildJson(this.m_connections, HAPSerializationFormat.JSON));
	}
}
