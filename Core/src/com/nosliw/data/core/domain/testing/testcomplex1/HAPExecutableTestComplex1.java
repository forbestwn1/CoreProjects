package com.nosliw.data.core.domain.testing.testcomplex1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPExecutableTestComplex1 extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityTestComplex1.ENTITY_TYPE;

	private Map<String, HAPExecutable> m_attributesSimple;
	
	public HAPExecutableTestComplex1() {
		super(ENTITY_TYPE);
		this.m_attributesSimple = new LinkedHashMap<String, HAPExecutable>();
	}
	
	public void setSimpleAttribute(String attrName, HAPExecutable attrExe) {
		this.m_attributesSimple.put(attrName, attrExe);
	}
	
}
