package com.nosliw.data.core.domain.entity.scriptbased;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute
public class HAPExecutableEntityScriptComplex extends HAPExecutableEntityComplex{

	@HAPAttribute
	public static final String SCRIPT = "script";

	private HAPJsonTypeScript m_script;

	public HAPExecutableEntityScriptComplex() {	}

	public void setScript(String script) {		this.setNormalAttributeObject(SCRIPT, new HAPEmbededExecutable(new HAPJsonTypeScript(script)));	}

}
