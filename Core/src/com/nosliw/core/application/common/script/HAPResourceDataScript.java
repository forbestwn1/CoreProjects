package com.nosliw.core.application.common.script;

import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDataImp;

@HAPEntityWithAttribute
public class HAPResourceDataScript extends HAPResourceDataImp implements HAPWithScript{

	private HAPJsonTypeScript m_script;

	public HAPResourceDataScript() {	}

	public void setScript(String script) {    this.m_script = new HAPJsonTypeScript(script);     }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		HAPJsonTypeScript script = this.m_script;
		if(script!=null){
			jsonMap.put(SCRIPT, script.toStringValue(HAPSerializationFormat.JSON_FULL));
			typeJsonMap.put(SCRIPT, script.getClass());
		}
	}
}
