package com.nosliw.data.core.domain.testing.testcomplex1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableTestComplex1 extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityTestComplex1.ENTITY_TYPE;

	@HAPAttribute
	public static String TESTSIMPLE1 = "testSimple1";

	private Map<String, HAPExecutable> m_attributes;
	
	public HAPExecutableTestComplex1() {
		super(ENTITY_TYPE);
		this.m_attributes = new LinkedHashMap<String, HAPExecutable>();
	}
	
	public void setAttribute(String attrName, HAPExecutable attrExe) {
		this.m_attributes.put(attrName, attrExe);
	}

	@Override
	protected void buildAttributeResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		for(String attrName : this.m_attributes.keySet()) {
			jsonMap.put(attrName, this.m_attributes.get(attrName).toResourceData(runtimeInfo).toString());
		}
	}
	
}
