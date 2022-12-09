package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutableWithValue;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;

@HAPEntityWithAttribute
public class HAPExecutableTestComplexScript extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityTestComplexScript.ENTITY_TYPE;

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String PARM = "parm";

	@HAPAttribute
	public static String VARIABLE = "variable";
	
	public HAPExecutableTestComplexScript() {
		super(HAPDefinitionEntityTestComplexScript.ENTITY_TYPE);
	}
	
	public void setScript(String script) {		this.setNormalAttribute(SCRIPT, new HAPEmbededExecutableWithValue(new HAPJsonTypeScript(script)));	}

	public void setParms(Map<String, Object> parms) {	this.setNormalAttribute(PARM, new HAPEmbededExecutableWithValue(parms));	}

	public void setVariables(List<HAPInfoReferenceResolve> vars) {    this.setNormalAttribute(PARM, new HAPEmbededExecutableWithValue(vars));	}
	
}
