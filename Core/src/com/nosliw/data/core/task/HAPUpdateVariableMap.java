package com.nosliw.data.core.task;

import java.util.Map;

public class HAPUpdateVariableMap implements HAPUpdateVariable{

	private Map<String, String> m_map;
	
	public HAPUpdateVariableMap(Map<String, String> map) {
		this.m_map = map;
	}
	
	@Override
	public String getUpdatedVariable(String varName) {
		return this.m_map.get(varName);
	}

}
