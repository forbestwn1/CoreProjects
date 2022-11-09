package com.nosliw.data.core.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.story.HAPReferenceElement;

public class HAPChangeItemPatch extends HAPChangeItemModifyElement{

	public static final String MYCHANGETYPE = HAPConstantShared.STORYDESIGN_CHANGETYPE_PATCH;
	
	@HAPAttribute
	public static final String PATH = "path";

	@HAPAttribute
	public static final String VALUE = "value";

	private String m_path;
	
	private Object m_value;
	
	public String getPath() {	return this.m_path;	}
	
	public Object getValue() {   return this.m_value;   }
	public void setValue(Object value) {   this.m_value = value;    }
	
	public HAPChangeItemPatch() {
		super(MYCHANGETYPE);
	}
	
	public HAPChangeItemPatch(HAPReferenceElement targetReference, String path, Object value) {
		super(MYCHANGETYPE, targetReference);
		this.m_path = path;
		this.m_value = value;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_path = jsonObj.getString(PATH);
		this.m_value = jsonObj.opt(VALUE);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.m_path);
		if(this.m_value!=null) {
			jsonMap.put(VALUE, HAPUtilityJson.buildJson(m_value, HAPSerializationFormat.JSON));
			typeJsonMap.put(VALUE, this.m_value.getClass());
		}
	}
}
