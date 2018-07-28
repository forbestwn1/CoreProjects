package com.nosliw.data.core.flow;

import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;

public class HAPUpdateVariableMap implements HAPUpdateVariable{

	private Map<String, String> m_map;
	
	public HAPUpdateVariableMap(Map<String, String> map) {
		this.m_map = map;
	}
	
	@Override
	public String getUpdatedVariable(String varName) {
		String out = this.m_map.get(varName);
		if(HAPBasicUtility.isStringEmpty(out))  out = varName;
		return out;
	}

}
