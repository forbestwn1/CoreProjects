package com.nosliw.data.core.domain.testing.testsimple1;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableTestSimple1 extends HAPExecutableImp{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	private HAPJsonTypeScript m_script;
	
	public HAPExecutableTestSimple1(String script) {
		this.m_script = new HAPJsonTypeScript(script);
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		jsonMap.put(SCRIPT, this.m_script.getScript());
		typeJsonMap.put(SCRIPT, HAPJsonTypeScript.class);
	}
}
