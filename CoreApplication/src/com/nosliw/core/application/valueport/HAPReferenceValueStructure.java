package com.nosliw.core.application.valueport;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPReferenceValueStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String ID = "id";

	//refer to name of value structure
	private String m_name;
	
	//refer to unique value structure definition id
	private String m_id;

	public String getName() {    return this.m_name;    }
	
	public String getId() {    return this.m_id;     }
	
	@Override
	protected boolean buildObjectByJson(Object obj){
		if(obj instanceof String) {
			this.m_id = (String)obj;
		}
		else if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			this.m_name = (String)jsonObj.opt(NAME);
			this.m_id = (String)jsonObj.opt(ID);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, m_name);
		jsonMap.put(ID, m_id);
	}
}
