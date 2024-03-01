package com.nosliw.uiresource.module.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public class HAPPresentUIActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String PAGE = "page";
	
	@HAPAttribute
	public static String SETTING = "setting";
	
	private String m_page;

	private JSONObject m_setting;
	
	public HAPPresentUIActivityDefinition(String type) {
		super(type);
	}
	
	public String getPage() {  return this.m_page;   }
	public void setPage(String ui) {   this.m_page = ui;    }
	
	public JSONObject getSetting() {   return this.m_setting;   }

	@Override
	protected void buildConfigureByJson(JSONObject configurJsonObj) {
		this.setPage(configurJsonObj.optString(PAGE));
		this.m_setting = configurJsonObj.optJSONObject(SETTING);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PAGE, this.m_page);
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPPresentUIActivityDefinition out = new HAPPresentUIActivityDefinition(this.getActivityType());
		this.cloneToNormalActivityDefinition(out);
		out.m_page = this.m_page;
		out.m_setting = this.m_setting;
		return out;
	}

	@Override
	public void parseActivityDefinition(Object obj, HAPManualEntityComplex complexEntity, HAPSerializationFormat format) {
		this.buildObject(obj, format);
	}

}
