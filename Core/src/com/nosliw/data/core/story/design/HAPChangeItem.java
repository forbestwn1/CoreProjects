package com.nosliw.data.core.story.design;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.story.HAPStory;

@HAPEntityWithAttribute
public abstract class HAPChangeItem extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String CHANGETYPE = "changeType";

	@HAPAttribute
	public static final String TARGETCATEGARY = "targetCategary";

	@HAPAttribute
	public static final String TARGETID = "targetId";

	private String m_changeType;
	
	private String m_targetCategary;
	
	private String m_targetId;

	private HAPStory m_story;
	
	public HAPChangeItem() {}
	
	public HAPChangeItem(String changeType, String targetCategary, String targetId) {
		this.m_changeType = changeType;
		this.m_targetCategary = targetCategary;
		this.m_targetId = targetId;
	}
	
	public String getChangeType() {    return this.m_changeType;    }
	
	public String getTargetCategary() {   return this.m_targetCategary;   }
	
	public String getTargetId() {   return this.m_targetId;    }
	
	public void setStory(HAPStory story) {    this.m_story = story;    }
	protected HAPStory getStory() {    return this.m_story;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_changeType = jsonObj.getString(CHANGETYPE);
		this.m_targetCategary = jsonObj.getString(TARGETCATEGARY);
		this.m_targetId = jsonObj.getString(TARGETID);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHANGETYPE, this.m_changeType);
		jsonMap.put(TARGETCATEGARY, this.m_targetCategary);
		jsonMap.put(TARGETID, this.m_targetId);
	}

}
