package com.nosliw.miniapp.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppEntry  extends HAPSerializableImp{

	@HAPAttribute
	public static final String MODULE = "module";

	@HAPAttribute
	public static final String PARM = "parm";
	
	//all modules in this entry
	private Map<String, HAPDefinitionMiniAppModule> m_modules;
	
	//parms to control the behavior of module
	private Map<String, Object> m_parms;
	
	public HAPDefinitionMiniAppEntry() {
		this.m_modules = new LinkedHashMap<String, HAPDefinitionMiniAppModule>();
		this.m_parms = new LinkedHashMap<String, Object>();
	}
	
	public Map<String, HAPDefinitionMiniAppModule> getModules(){  return this.m_modules;  }
	public Map<String, Object> getParms(){   return this.m_parms;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_modules, HAPSerializationFormat.JSON));
		jsonMap.put(PARM, HAPJsonUtility.buildJson(this.m_parms, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_modules =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppModule.class.getName(), jsonObj.optJSONObject(MODULE));
		
		JSONObject parmJsonObj = jsonObj.optJSONObject(PARM);
		for(Object key : parmJsonObj.keySet()) {
			String parmName = (String)key;
			this.m_parms.put(parmName, parmJsonObj.opt(parmName));
		}
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
	
}
