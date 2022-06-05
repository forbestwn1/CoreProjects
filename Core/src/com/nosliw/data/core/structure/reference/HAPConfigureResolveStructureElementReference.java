package com.nosliw.data.core.structure.reference;

import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;

//configure for resolve 
public class HAPConfigureResolveStructureElementReference extends HAPSerializableImp{

	//only within these group types, if empty or null, then all of group type is valid
	public Set<String> valueStructureGroupTypes;
	
	//different strategy (first or best match)
	public String searchMode = HAPConstant.RESOLVEPARENTMODE_BEST;
	
	//candidate element type (constant, value, data, node)
	public Set<String> candidateElementTypes;

	
	public void mergeHard(HAPConfigureResolveStructureElementReference configure) {
		
	}
}
