package com.nosliw.data.core.story.element.connection;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPConnectionImp;

@HAPEntityWithAttribute
public class HAPConnectionContain extends HAPConnectionImp{
	
	public final static String CONNECTION_TYPE = HAPConstant.STORYCONNECTION_TYPE_CONTAIN; 
	
	@HAPAttribute
	public static final String CHILDID = "childId";

	private String m_childId;
	
	public HAPConnectionContain() {
		super(CONNECTION_TYPE);
	}

	public String getChildId() {    return this.m_childId;    }
	public void setChildId(String childId) {   this.m_childId = childId;   }
	
	@Override
	public void patch(String path, Object value) {
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
