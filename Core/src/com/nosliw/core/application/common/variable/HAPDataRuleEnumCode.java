package com.nosliw.core.application.common.variable;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPDataRuleEnumCode extends HAPDataRuleEnum{

	@HAPAttribute
	public static String ENUMCODE = "enumCode";

	private String m_enumCode;

	public HAPDataRuleEnumCode() {}

	public HAPDataRuleEnumCode(String enumCode) {
		this.m_enumCode = enumCode;
	}

	public String getEnumCode() {    return this.m_enumCode;    }
	
	@Override
	public HAPServiceData verify(HAPData data, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENUMCODE, this.getEnumCode());
	}
	
	@Override
	public boolean buildObjectByJson(Object value) {
		JSONObject valueObj = (JSONObject)value;
		super.buildObjectByJson(valueObj);
		this.m_enumCode = valueObj.getString(ENUMCODE);
		return true;
	}
}
