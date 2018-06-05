package com.nosliw.miniapp.data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public abstract class HAPInstanceMiniAppData extends HAPSerializableImp{

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String DATA = "data";
	
	abstract public String getType();
	
	private Object m_data;
	
	public Object getData() {   return this.m_data;   }
	public String getDataStr() { return this.m_data.toString();  }
	public void setData(Object data) {   this.m_data = data;   }

	public static Map<String, Class> m_dataInstanceClasses = new LinkedHashMap<String, Class>();
	
	static {
		m_dataInstanceClasses.put(HAPConstant.MINIAPPDATA_TYPE_SETTING, HAPInstanceMiniAppDataSetting.class);
	}
	
	public static HAPInstanceMiniAppData buildObject(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;
		String type = (String)jsonObj.get(TYPE);
		HAPInstanceMiniAppData out = (HAPInstanceMiniAppData)HAPSerializeManager.getInstance().buildObject(m_dataInstanceClasses.get(type).getName(), jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(DATA, this.getDataStr());
	}
	

	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_data = jsonObj.optJSONObject(DATA);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}

}
