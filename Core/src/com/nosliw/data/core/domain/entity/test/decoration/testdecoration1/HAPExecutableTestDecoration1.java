package com.nosliw.data.core.domain.entity.test.decoration.testdecoration1;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutableWithValue;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute
public class HAPExecutableTestDecoration1 extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityTestDecoration1.ENTITY_TYPE;

	@HAPAttribute
	public static final String SCRIPT = "script";

	private HAPJsonTypeScript m_script;

	public HAPExecutableTestDecoration1() {
		super(ENTITY_TYPE);
	}

	public void setScript(String script) {		this.setNormalAttribute(SCRIPT, new HAPEmbededExecutableWithValue(new HAPJsonTypeScript(script)));	}
	
}
