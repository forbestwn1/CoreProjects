package com.nosliw.data.core.structure.reference;

import java.util.Set;

public class HAPConfigureResolveStructureElementReference {

	//only within these group types, if empty or null, then all of group type is valid
	private Set<String> m_valueStructureroupTypes;
	
	private String m_searchMode;
	
	private Boolean m_relativeInheritRule; 
	
	private Set<String> m_candidateElementTypes;

	public String getSearchMode() {    return this.m_searchMode;   }
	public void setSearchMode(String searchMode) {    this.m_searchMode = searchMode;    }

	public Set<String> getValueStructureGroupTypes(){   return this.m_valueStructureroupTypes;    }

	public Set<String> getCandidateElementTypes(){    return this.m_candidateElementTypes;    }
	
}
