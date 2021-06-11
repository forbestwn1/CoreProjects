package com.nosliw.data.core.structure;

import java.util.List;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;

public interface HAPStructure extends HAPWithConstantScript, HAPSerializable{

	String getStructureType();
	
	HAPRootStructure getRoot(String localId);

	List<HAPRootStructure> getAllRoots();
	
	//resolve root by reference, 
	List<HAPRootStructure> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist);

	List<HAPInfoAlias> discoverRootAliasById(String id);
	HAPReferenceRoot getRootReferenceById(String id);
	
	HAPRootStructure addRoot(HAPReferenceRoot rootReference, HAPRootStructure root);
	
	void updateRootName(HAPUpdateName updateName);
	
	void processed();
	
	HAPInfo getInfo();
	void setInfo(HAPInfo info);
	
	HAPStructure cloneStructure();

}
