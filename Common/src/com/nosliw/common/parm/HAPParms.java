package com.nosliw.common.parm;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParms extends HAPSerializableImp{

	private Map<String, Object> m_parmValues = new LinkedHashMap<String, Object>();
	
	public Set<String> getParmNames(){
		return this.m_parmValues.keySet();
	}
	
	public Object getParmValue(String name) {
		return this.m_parmValues.get(name);
	}

	public void setParm(String name, Object vlaue) {
		this.m_parmValues.put(name, vlaue);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String key : this.m_parmValues.keySet()) {
			jsonMap.put(key, HAPManagerSerialize.getInstance().toStringValue(this.m_parmValues.get(key), HAPSerializationFormat.JSON));
			typeJsonMap.put(key, this.m_parmValues.get(key).getClass());
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		Iterator<String> it = jsonObj.keys();
		while(it.hasNext()){
			String key = it.next();
			Object value = jsonObj.opt(key);
			this.setParm(key, value);
		}
		return true;
	}
}
