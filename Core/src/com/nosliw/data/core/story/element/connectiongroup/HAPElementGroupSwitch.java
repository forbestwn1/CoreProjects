package com.nosliw.data.core.story.element.connectiongroup;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPElementGroupImp;

public class HAPElementGroupSwitch extends HAPElementGroupImp{

	public final static String GROUP_TYPE = HAPConstant.STORYGROUP_TYPE_SWITCH; 

	@HAPAttribute
	public static final String CHOICE = "choice";

	private String m_choice;

	public String getChoice() {    return this.m_choice;     }
	public void setChoice(String choice) {     this.m_choice = choice;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_choice = (String)jsonObj.opt(CHOICE);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHOICE, HAPJsonUtility.buildJson(this.m_choice+"", HAPSerializationFormat.JSON));
	}
	
	@Override
	public void patch(String path, Object value) {
		if(CHOICE.equals(path)) {
			this.m_choice = (String)value;
		}
	}
	
}
