package com.nosliw.common.updatename;

import java.util.Map;

import com.nosliw.common.path.HAPComplexPath;

public class HAPUpdateNameMapRoot implements HAPUpdateName{

	private Map<String, String> m_map;
	
	public HAPUpdateNameMapRoot(Map<String, String> map) {
		this.m_map = map;
	}
	
	@Override
	public String getUpdatedName(String name) {
		HAPComplexPath path = new HAPComplexPath(name);
		String updatedRootName = this.m_map.get(path.getRootName());
		HAPComplexPath updatedPath = new HAPComplexPath(updatedRootName, path.getPathStr());
		return updatedPath.getFullName();
	}

}
