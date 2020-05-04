package com.nosliw.data.core.resource.dynamic.story;

import java.util.HashSet;
import java.util.Set;

public class HAPStoryNodeTypeInfo {

	private Set<String> m_profiles;
	
	private String m_type;
	
	public HAPStoryNodeTypeInfo(String type) {
		this.m_type = type;
		this.m_profiles = new HashSet<String>();
	}
	
	//type of node
	public String getType() {  return this.m_type;  }
	
	//profile
	public Set<String> getProfile(){    return this.m_profiles;    }

}
