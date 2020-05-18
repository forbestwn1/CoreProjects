package com.nosliw.data.core.story.element.connection;

import org.json.JSONObject;

public class ConnectionEntityContain {
	
	public static final String CHILD = "child";

	private JSONObject m_entity;
	
	public ConnectionEntityContain(Object entity) {
		this.m_entity = (JSONObject)entity;
	}

	public Object getChildId() {    return this.m_entity.optString(CHILD);    }
}
