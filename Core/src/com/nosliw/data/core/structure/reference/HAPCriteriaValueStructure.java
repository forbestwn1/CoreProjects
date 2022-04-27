package com.nosliw.data.core.structure.reference;

import java.util.Set;

public class HAPCriteriaValueStructure {

	//only within these group types, if empty or null, then all of group type is valid
	private Set<String> m_groupTypes;
	
	//refer to name of value structure
	private String m_name;
	
	//refer to unique value structure definition id
	private String m_definitionId;

	public Set<String> getGroupTypes(){   return this.m_groupTypes;    }

	public String getName() {    return this.m_name;    }
	
	public String getDefinitionId() {    return this.m_definitionId;     }
	
}
