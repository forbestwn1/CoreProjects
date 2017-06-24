package com.nosliw.common.info;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public class HAPInfoImpSimple extends HAPSerializableImp implements HAPInfo{

	private Map<String, Object> m_values = new LinkedHashMap<String, Object>();
	
	@Override
	public Object getValue(String name) {		return this.m_values.get(name);	}

	@Override
	public void setValue(String name, Object value) {		this.m_values.put(name, value);	}

	@Override
	public Set<String> getNames() {   return this.m_values.keySet();  }

	@Override
	protected String buildFullJson(){	return HAPSerializeManager.getInstance().toStringValue(m_values, HAPSerializationFormat.JSON_FULL);	}

	@Override
	protected String buildJson(){	return HAPSerializeManager.getInstance().toStringValue(m_values, HAPSerializationFormat.JSON);	}
	
}
