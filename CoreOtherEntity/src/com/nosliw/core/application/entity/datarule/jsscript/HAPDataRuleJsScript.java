package com.nosliw.core.application.entity.datarule.jsscript;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPDataRule;

@HAPEntityWithAttribute
public class HAPDataRuleJsScript extends HAPDataRule{

	@HAPAttribute
	public static String SCRIPT = "script";

	private String m_script;
	
	public HAPDataRuleJsScript() {
		super(HAPConstantShared.DATARULE_TYPE_JSSCRIPT);
	}

	public HAPDataRuleJsScript(String script) {
		this();
		this.m_script = script;
	}

	public String getScript() {    return this.m_script;    }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPT, this.getScript());
	}
	
	@Override
	public boolean buildObjectByJson(Object value) {
		JSONObject valueObj = (JSONObject)value;
		super.buildObjectByJson(valueObj);
		this.m_script = valueObj.getString(SCRIPT);
		return true;
	}

}
