package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;

//connect end connect to a profile on a node 
public class HAPConnectionEnd extends HAPSerializableImp{

	public static final String CONNECTIONID = "connectionId";
	public static final String NODEID = "nodeId";
	public static final String PROFILE = "profile";
	
	private String m_connectionId;
	
	private String m_nodeId;
	
	private String m_profile;

	private boolean m_ifDeleteNode;

	public String getConnectionId() {    return this.m_connectionId;    }
	public void setConnectionId(String connectionId) {   this.m_connectionId = connectionId;    }
	
	public String getNodeId() {   return this.m_nodeId;   }
	
	public String getProfile() {    return this.m_profile;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_nodeId = jsonObj.getString(NODEID);
		this.m_profile = (String)jsonObj.opt(PROFILE);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NODEID, this.m_nodeId);
		jsonMap.put(CONNECTIONID, this.m_connectionId);
		jsonMap.put(PROFILE, this.m_profile);
	}

}
