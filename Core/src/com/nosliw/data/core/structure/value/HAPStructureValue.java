package com.nosliw.data.core.structure.value;

import java.util.List;

import com.nosliw.data.core.structure.HAPConfigureReferenceResolve;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPStructure;

//
public interface HAPStructureValue extends HAPStructure{

	//resolve root by reference, 
	List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, HAPConfigureReferenceResolve configure, boolean createIfNotExist);
	
}
