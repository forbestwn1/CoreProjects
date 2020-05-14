package com.nosliw.data.core.story;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class HAPStoryNodeImp extends HAPStoryElementImp implements HAPStoryNode{

	private Set<String> m_connections;
	
	public HAPStoryNodeImp() {
		this.m_connections = new HashSet<String>();
	}

	public HAPStoryNodeImp(String type) {
		super(type);
	}
	
	@Override
	public Set<String> getConnections() {  return this.m_connections;  }

	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		
		JSONArray connectionJsonArray = jsonObj.optJSONArray(CONNECTIONS);
		for(int i=0; i<connectionJsonArray.length(); i++) {
			this.m_connections.add(connectionJsonArray.getString(i));
		}
		return true;  
	}

}
