package com.nosliw.data.core.domain.testing.testsimple1;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.complex.HAPExecutableEntitySimple;

@HAPEntityWithAttribute
public class HAPExecutableTestSimple1 extends HAPExecutableEntitySimple{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String PARM = "parm";
	
	public HAPExecutableTestSimple1(String script, Map<String, Object> parms) {
		super(HAPDefinitionEntityTestSimple1.ENTITY_TYPE);
		this.setAttribute(SCRIPT, new HAPJsonTypeScript(script));
		this.setAttribute(PARM, parms);
	}
}
