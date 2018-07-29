package com.nosliw.miniapp.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppModule  extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROLE = "role";

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String MODULE = "module";
	
	@HAPAttribute
	public static final String PARM = "parm";

	private String m_role;
	
	private String m_name;
	
	private String m_module;
	
	private Map<String, Object> m_parms;

	public HAPDefinitionMiniAppModule() {
		this.m_parms = new LinkedHashMap<String, Object>();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROLE, this.m_role);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(MODULE, this.m_module);
		jsonMap.put(PARM, HAPJsonUtility.buildJson(this.m_parms, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_role = (String)jsonObj.opt(ROLE);
		this.m_name = (String)jsonObj.opt(NAME);
		this.m_module = (String)jsonObj.opt(MODULE);

		JSONObject parmJsonObj = jsonObj.optJSONObject(PARM);
		for(Object key : parmJsonObj.keySet()) {
			String parmName = (String)key;
			this.m_parms.put(parmName, parmJsonObj.opt(parmName));
		}
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
}
