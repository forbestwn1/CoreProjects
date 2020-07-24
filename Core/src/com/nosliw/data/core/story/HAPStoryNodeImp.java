package com.nosliw.data.core.story;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPStoryNodeImp extends HAPStoryElementImp implements HAPStoryNode{

	private Set<String> m_connections;
	
	public HAPStoryNodeImp() {
		super(HAPConstant.STORYELEMENT_CATEGARY_NODE);
		this.m_connections = new HashSet<String>();
	}

	public HAPStoryNodeImp(String type) {
		super(HAPConstant.STORYELEMENT_CATEGARY_NODE, type);
	}
	
	@Override
	public Set<String> getConnections() {  return this.m_connections;  }
 
	@Override
	public void addConnection(String connectionId) {  this.m_connections.add(connectionId);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONNECTIONS, HAPJsonUtility.buildJson(this.m_connections, HAPSerializationFormat.JSON));
	}

}
