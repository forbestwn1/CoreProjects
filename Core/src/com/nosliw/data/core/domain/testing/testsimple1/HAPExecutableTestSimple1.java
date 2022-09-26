package com.nosliw.data.core.domain.testing.testsimple1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPExecutableEntitySimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableTestSimple1 extends HAPExecutableEntitySimple{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String PARM = "parm";
	
	private HAPJsonTypeScript m_script;
	
	private Map<String, Object> m_parms = new LinkedHashMap<String, Object>();
	
	public HAPExecutableTestSimple1(String script, Map<String, Object> parms) {
		super(HAPDefinitionEntityTestSimple1.ENTITY_TYPE);
		this.m_script = new HAPJsonTypeScript(script);
		this.m_parms.putAll(parms);
	}
	
	@Override
	protected void buildAttributeResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		jsonMap.put(PARM, HAPJsonUtility.buildJson(m_parms, HAPSerializationFormat.JSON));
		
		jsonMap.put(SCRIPT, this.m_script.getScript());
		typeJsonMap.put(SCRIPT, HAPJsonTypeScript.class);
	}
}
