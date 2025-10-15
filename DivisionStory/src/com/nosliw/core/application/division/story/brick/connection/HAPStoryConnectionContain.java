package com.nosliw.core.application.division.story.brick.connection;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.xxx.brick.HAPStoryConnectionImp;

@HAPEntityWithAttribute
public class HAPStoryConnectionContain extends HAPStoryConnectionImp{
	
	public final static String CONNECTION_TYPE = HAPConstantShared.STORYCONNECTION_TYPE_CONTAIN; 
	
	@HAPAttribute
	public static final String CHILDID = "childId";

	private String m_childId;
	
	public HAPStoryConnectionContain() {
		super(CONNECTION_TYPE);
	}

	public String getChildId() {    return this.m_childId;    }
	public void setChildId(String childId) {   this.m_childId = childId;   }
	
	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryConnectionContain out = new HAPStoryConnectionContain();
		super.cloneTo(out);
		out.m_childId = this.m_childId;
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_childId = (String)jsonObj.opt(CHILDID);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHILDID, this.m_childId);
	}
}
