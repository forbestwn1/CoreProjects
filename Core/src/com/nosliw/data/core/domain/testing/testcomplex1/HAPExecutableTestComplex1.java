package com.nosliw.data.core.domain.testing.testcomplex1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.testing.testsimple1.HAPExecutableTestSimple1;

@HAPEntityWithAttribute
public class HAPExecutableTestComplex1 extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityTestComplex1.ENTITY_TYPE;

	@HAPAttribute
	public static String TESTSIMPLE1 = "testSimple1";

	private Map<String, HAPExecutableTestSimple1> m_testSimple1Attribute;
	
	public HAPExecutableTestComplex1() {
		super(ENTITY_TYPE);
		this.m_testSimple1Attribute = new LinkedHashMap<String, HAPExecutableTestSimple1>();
	}
	
	public void setTestSimpleAttribute(String attrName, HAPExecutableTestSimple1 attrExe) {
		this.m_testSimple1Attribute.put(attrName, attrExe);
	}
	
	
	
}
