package com.nosliw.data.core.story.element.connection;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPConnectionEntityContain {
	
	@HAPAttribute
	public static final String CHILD = "child";

	private JSONObject m_entity;
	
	public HAPConnectionEntityContain(Object entity) {
		this.m_entity = (JSONObject)entity;
	}

	public Object getChildId() {    return this.m_entity.optString(CHILD);    }
}
