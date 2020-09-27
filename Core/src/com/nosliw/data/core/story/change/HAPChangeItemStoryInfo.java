package com.nosliw.data.core.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;

public class HAPChangeItemStoryInfo extends HAPChangeItem{

	@HAPAttribute
	public static final String INFONAME = "infoName";

	@HAPAttribute
	public static final String INFOVALUE = "infoValue";

	private String m_infoName;
	
	private Object m_infoValue;
	
	public HAPChangeItemStoryInfo() {
		super(HAPConstant.STORYDESIGN_CHANGETYPE_STORYINFO);
	}
	
	public HAPChangeItemStoryInfo(String infoName, Object infoValue) {
		this();
		this.m_infoName = infoName;
		this.m_infoValue = infoValue;
	}
	
	public String getInfoName() {    return this.m_infoName;    }
	public Object getInfoValue() {    return this.m_infoValue;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_infoName = jsonObj.getString(INFONAME);
		this.m_infoValue = jsonObj.getString(INFOVALUE);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFONAME, this.m_infoName);
		jsonMap.put(INFOVALUE, this.m_infoValue+"");
		typeJsonMap.put(INFOVALUE, this.m_infoValue.getClass());
	}
}
