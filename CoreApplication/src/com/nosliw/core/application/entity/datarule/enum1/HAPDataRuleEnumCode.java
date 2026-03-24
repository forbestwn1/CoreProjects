package com.nosliw.core.application.entity.datarule.enum1;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.entity.datarule.HAPDataRule;

public class HAPDataRuleEnumCode extends HAPDataRuleEnum{

	@HAPAttribute
	public static String ENUMCODE = "enumCode";

	private String m_enumCode;

	public HAPDataRuleEnumCode() {}

	public String getEnumCode() {    return this.m_enumCode;    }
	public void setEnumCode(String enumCode) {   this.m_enumCode = enumCode;     }

	@Override
	public HAPDataRule cloneDataRule() {
		HAPDataRuleEnumCode out = new HAPDataRuleEnumCode();
		this.cloneToDataRule(out);
		out.m_enumCode = this.m_enumCode;
		return out;
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
