package com.nosliw.core.application.entity.datarule.enum1;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPDataRuleEnumCode extends HAPDataRuleEnum{

	@HAPAttribute
	public static String ENUMCODE = "enumCode";

	private String m_enumCode;

	public HAPDataRuleEnumCode() {}

	public String getEnumCode() {    return this.m_enumCode;    }
	public void setEnumCode(String enumCode) {   this.m_enumCode = enumCode;     }
	
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
