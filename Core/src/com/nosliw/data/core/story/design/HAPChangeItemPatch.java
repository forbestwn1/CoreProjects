package com.nosliw.data.core.story.design;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPChangeItemPatch extends HAPChangeItem{

	public static final String MYCHANGETYPE = HAPConstant.STORYDESIGN_CHANGETYPE_PATCH;
	
	@HAPAttribute
	public static final String PATH = "path";

	@HAPAttribute
	public static final String VALUE = "value";

	private String m_path;
	
	private Object m_value;
	
	public String getPath() {	return this.m_path;	}
	
	public Object getValue() {   return this.m_value;   }

	public HAPChangeItemPatch() {}
	
	public HAPChangeItemPatch(String targetCategary, String targetId, String path, Object value) {
		super(MYCHANGETYPE, targetCategary, targetId);
		this.m_path = path;
		this.m_value = value;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_path = jsonObj.getString(PATH);
		this.m_value = jsonObj.opt(VALUE);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.m_path);
		jsonMap.put(VALUE, HAPJsonUtility.buildJson(m_value, HAPSerializationFormat.JSON));
	}

}
