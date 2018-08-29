package com.nosliw.common.updatename;

import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;

public class HAPUpdateNameMap implements HAPUpdateName{

	private Map<String, String> m_map;
	
	public HAPUpdateNameMap(Map<String, String> map) {
		this.m_map = map;
	}
	
	@Override
	public String getUpdatedName(String varName) {
		String out = this.m_map.get(varName);
		if(HAPBasicUtility.isStringEmpty(out))  out = varName;
		return out;
	}

}
