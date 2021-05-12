package com.nosliw.data.core.structure;

import java.util.List;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;

public interface HAPStructure extends HAPWithConstantScript{

	String getStructureType();
	
	HAPRoot getRoot(String localId);

	List<HAPRoot> getAllRoots();
	
	//resolve root by reference, 
	List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist);
	Set<HAPInfoName> discoverRootNameById(String id);

	HAPRoot addRoot(HAPReferenceRoot rootReference, HAPRoot root);
	
	void updateRootName(HAPUpdateName updateName);
	
	HAPStructure cloneStructure();

}
