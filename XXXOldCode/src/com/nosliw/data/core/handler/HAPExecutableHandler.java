package com.nosliw.data.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

@HAPEntityWithAttribute
public class HAPExecutableHandler extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String STEPS = "steps";

	private List<String> m_steps;
	
	public HAPExecutableHandler(HAPHandler handlerDef) {
		this.m_steps = new ArrayList<String>();
		this.m_steps.addAll(handlerDef.getSteps());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STEPS, HAPUtilityJson.buildJson(this.m_steps, HAPSerializationFormat.JSON));
	}

}
