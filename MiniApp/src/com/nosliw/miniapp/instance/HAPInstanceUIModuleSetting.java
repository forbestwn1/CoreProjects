package com.nosliw.miniapp.instance;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.data.core.HAPDataWrapper;

@HAPEntityWithAttribute
public class HAPInstanceUIModuleSetting  extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String SETTING = "setting";

	private String m_id;
	
	private String m_name;
	
	private Map<String, Map<String, Object>> m_setting;

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(SETTING, HAPJsonUtility.buildJson(this.m_setting, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = (String)jsonObj.opt(ID);
		this.m_name = (String)jsonObj.opt(NAME);
		this.m_setting = HAPSerializeUtility.buildMapFromJsonObject(HAPDataWrapper.class.getName(), jsonObj.optJSONObject(SETTING));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}

}
