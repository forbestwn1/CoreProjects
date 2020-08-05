package com.nosliw.uiresource.page.tag;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPUITagQueryResult extends HAPSerializableImp{

	@HAPAttribute
	public static final String TAG = "tag";

	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";

	private String m_tag;
	
	private Map<String, String> m_attributes;

	public HAPUITagQueryResult() {
		this.m_attributes = new LinkedHashMap<String, String>();
	}

	public HAPUITagQueryResult(String tag) {
		this();
		this.m_tag = tag;
	}
	
	public String getTag() {    return this.m_tag;    }
	
	public Map<String, String> getAttributes(){   return this.m_attributes;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TAG, this.m_tag);
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildJson(this.m_attributes, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_tag = jsonObj.getString(TAG);
		JSONObject attrsObj = jsonObj.optJSONObject(ATTRIBUTES);
		if(attrsObj!=null) {
			for(Object key : attrsObj.keySet()) {
				this.m_attributes.put((String)key, attrsObj.getString((String)key));
			}
		}
		return true;  
	}	
}
