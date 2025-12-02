package com.nosliw.core.application.division.story.xxx.brick;

import java.util.HashSet;
import java.util.Set;

public class HAPStoryDefinitionStoryNode {

	//all the profile
	private Set<HAPStoryDefinitionNodeProfile> m_profiles;
	
	//type name
	private String m_type;
	
	public HAPStoryDefinitionStoryNode(String type) {
		this.m_type = type;
		this.m_profiles = new HashSet<HAPStoryDefinitionNodeProfile>();
	}
	
	//type of node
	public String getType() {  return this.m_type;  }
	
	//profile
	public Set<HAPStoryDefinitionNodeProfile> getProfile(){    return this.m_profiles;    }

}
