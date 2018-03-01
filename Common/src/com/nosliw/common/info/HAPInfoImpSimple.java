package com.nosliw.common.info;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public class HAPInfoImpSimple extends HAPSerializableImp implements HAPInfo{

	private Map<String, Object> m_values = new LinkedHashMap<String, Object>();
	
	@Override
	public Object getValue(String name) {		return this.m_values.get(name);	}

	@Override
	public void setValue(String name, Object value) {		this.m_values.put(name, value);	}

	@Override
	public Set<String> getNames() {   return this.m_values.keySet();  }

	@Override
	protected String buildFullJson(){	return HAPSerializeManager.getInstance().toStringValue(m_values, HAPSerializationFormat.JSON_FULL);	}

	@Override
	protected String buildJson(){	return HAPSerializeManager.getInstance().toStringValue(m_values, HAPSerializationFormat.JSON);	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		Iterator<String> it = jsonObj.keys();
		while(it.hasNext()){
			String key = it.next();
			String value = jsonObj.optString(key);
			this.setValue(key, value);
		}
		return true;
	}
}
