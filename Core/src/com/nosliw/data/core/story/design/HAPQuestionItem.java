package com.nosliw.data.core.story.design;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;

public class HAPQuestionItem extends HAPQuestion{

	@HAPAttribute
	public static final String TARGETCATEGARY = "targetCategary";

	@HAPAttribute
	public static final String TARGETID = "targetId";

	private String m_targetId;

	private String m_targetCategary;
	
	public HAPQuestionItem() {}
	
	public HAPQuestionItem(String question, String targetCategary, String targetId) {
		super(question);
	}

	@Override
	public String getType() {	return HAPConstant.STORYDESIGN_QUESTIONTYPE_ITEM;	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_targetCategary = (String)jsonObj.opt(TARGETCATEGARY);
		this.m_targetId = (String)jsonObj.opt(TARGETID);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGETCATEGARY, this.m_targetCategary);
		jsonMap.put(TARGETID, this.m_targetId);
	}

}
