package com.nosliw.data.core.domain.entity.configure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableConfigure extends HAPExecutableImp{

	@HAPAttribute
	public static final String SCRIPT = "script";

	private HAPJsonTypeScript m_script;

	public HAPExecutableConfigure() {	}

	public void setScript(String script) {    this.m_script = new HAPJsonTypeScript(script);     }
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		HAPJsonTypeScript script = this.m_script;
		if(script!=null){
			jsonMap.put(SCRIPT, script.toStringValue(HAPSerializationFormat.JSON_FULL));
			typeJsonMap.put(SCRIPT, script.getClass());
		}
	}
}
