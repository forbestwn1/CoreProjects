package com.nosliw.data.core.story.design;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.story.HAPWithAlias;

@HAPEntityWithAttribute
public abstract class HAPQuestion extends HAPEntityInfoImp implements HAPWithAlias{

	@HAPAttribute
	public static final String QUESTION = "question";

	@HAPAttribute
	public static final String TYPE = "type";

	private String m_question;
	
	public HAPQuestion() {}
	
	public HAPQuestion(String question) {
		this.m_question = question;
	}
	
	abstract public String getType(); 
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_question = (String)jsonObj.opt(QUESTION);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(QUESTION, this.m_question);
		jsonMap.put(TYPE, this.getType());
	}

	public static HAPQuestion parseQuestion(JSONObject jsonObj) {
		HAPQuestion out = null;
		String type = jsonObj.getString(TYPE);
		if(HAPConstantShared.STORYDESIGN_QUESTIONTYPE_GROUP.equals(type)) {
			out = new HAPQuestionGroup();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		else if(HAPConstantShared.STORYDESIGN_QUESTIONTYPE_ITEM.equals(type)) {
			out = new HAPQuestionItem();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
}
