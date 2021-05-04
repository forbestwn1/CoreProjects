package com.nosliw.data.core.structure;

import java.util.List;

public interface HAPStructure {

	String getStructureType();
	
	HAPRoot getRoot(String localId);

	List<HAPRoot> getAllRoots();
	
	//resolve root by reference, 
	List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist);

	HAPStructure cloneStructure();

}
