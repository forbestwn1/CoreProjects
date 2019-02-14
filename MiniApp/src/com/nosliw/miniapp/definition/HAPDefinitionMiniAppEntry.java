package com.nosliw.miniapp.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.uiresource.common.HAPComponentWithConfiguration;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppEntry  extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String MODULE = "module";

	//all modules in this entry
	private List<HAPDefinitionMiniAppModule> m_modules;

	private HAPComponentWithConfiguration m_configure;
	

	public HAPDefinitionMiniAppEntry() {
		this.m_modules = new ArrayList<HAPDefinitionMiniAppModule>();
	}
	
	public List<HAPDefinitionMiniAppModule> getModules(){  return this.m_modules;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_modules, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_modules =  HAPSerializeUtility.buildListFromJsonArray(HAPDefinitionMiniAppModule.class.getName(), jsonObj.optJSONArray(MODULE));
		return true;
	}
	
}
