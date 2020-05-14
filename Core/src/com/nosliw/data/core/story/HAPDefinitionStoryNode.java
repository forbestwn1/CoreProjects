package com.nosliw.data.core.story;

import java.util.HashSet;
import java.util.Set;

public class HAPDefinitionStoryNode {

	//all the profile
	private Set<HAPDefinitionNodeProfile> m_profiles;
	
	//type name
	private String m_type;
	
	public HAPDefinitionStoryNode(String type) {
		this.m_type = type;
		this.m_profiles = new HashSet<HAPDefinitionNodeProfile>();
	}
	
	//type of node
	public String getType() {  return this.m_type;  }
	
	//profile
	public Set<HAPDefinitionNodeProfile> getProfile(){    return this.m_profiles;    }

}
