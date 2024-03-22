package com.nosliw.core.application.common.valueport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueStructure;

//configure for resolve 
public class HAPConfigureResolveElementReference extends HAPInfoImpSimple{

	//only within these group types, if empty or null, then all of group type is valid
	private static final String GROUPTYPE = "groupType";

	//different strategy (first or best match)
	private static final String SEARCHMODE = "searchMode";

	//candidate element type (constant, value, data, node)
	private static final String ELEMENTTYPE = "elementType";
	
	
	//only within these group types, if empty or null, then all of group type is valid
	public Set<String> valueStructureGroupTypes;
	
	//different strategy (first or best match)
	public String searchMode = HAPConstant.RESOLVEPARENTMODE_BEST;
	
	//candidate element type (constant, value, data, node)
	public Set<String> candidateElementTypes;

	public Set<String> getGroupTypes(){   return (Set<String>)this.getValue(GROUPTYPE);    }
	public void setGroupTypes(Set<String> groupTyps) {    this.setValue(GROUPTYPE, groupTyps);    }
	
	public String getSearchMode() {    return (String)this.getValue(SEARCHMODE);     }
	public void setSearchMode(String searchMode) {   this.setValue(SEARCHMODE, searchMode);    }
	
	public Set<String> getElementTypes(){   return (Set<String>)this.getValue(ELEMENTTYPE);    }
	public void setElementTypes(Set<String> elementTypes) {     this.setValue(ELEMENTTYPE, elementTypes);       }
	
	public HAPConfigureResolveElementReference() {
		this.setSearchMode(HAPConstant.RESOLVEPARENTMODE_BEST);
		this.setGroupTypes(new HashSet<String>(Arrays.asList(HAPUtilityValueStructure.getVisibleToChildCategaries())));
	}
	
	
	public void mergeHard(HAPConfigureResolveElementReference configure) {
		
	}
}
