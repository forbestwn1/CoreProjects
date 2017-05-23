package com.nosliw.common.info;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPInfoImpSimple extends HAPSerializableImp implements HAPInfo{

	private Map<String, String> m_values = new LinkedHashMap<String, String>();
	
	@Override
	public String getValue(String name) {		return this.m_values.get(name);	}

	@Override
	public void setValue(String name, String value) {		this.m_values.put(name, value);	}

	@Override
	public Set<String> getNames() {   return this.m_values.keySet();  }

	@Override
	protected String buildFullJson(){ return HAPJsonUtility.buildMapJson(m_values); }

}
