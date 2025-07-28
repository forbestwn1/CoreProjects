package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;

public class HAPExecutableUIUnitPage extends HAPExecutableUIUnit{

	@HAPAttribute
	public static final String STYLE = "style";
	
	private HAPExecutableStyle m_style;

	public void setStyle(HAPExecutableStyle style) {
		this.m_style = style;
	}
	
	public HAPExecutableUIUnitPage(HAPDefinitionUIUnit uiUnitDefinition, String id) {
		super(uiUnitDefinition, id);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STYLE, HAPUtilityJson.buildJson(this.m_style, HAPSerializationFormat.JSON));
	}
}
