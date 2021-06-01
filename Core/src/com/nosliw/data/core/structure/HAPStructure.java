package com.nosliw.data.core.structure;

import java.util.List;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.updatename.HAPUpdateName;

public interface HAPStructure extends HAPWithConstantScript{

	String getStructureType();
	
	HAPRoot getRoot(String localId);

	List<HAPRoot> getAllRoots();
	
	//resolve root by reference, 
	List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist);

	List<HAPInfoAlias> discoverRootAliasById(String id);
	HAPReferenceRoot getRootReferenceById(String id);
	
	HAPRoot addRoot(HAPReferenceRoot rootReference, HAPRoot root);
	
	void updateRootName(HAPUpdateName updateName);
	
	void processed();
	
	HAPInfo getInfo();
	void setInfo(HAPInfo info);
	
	HAPStructure cloneStructure();

}
