package com.nosliw.data.core.structure;

import com.nosliw.common.serialization.HAPSerializable;

public interface HAPReferenceRoot extends HAPSerializable{

	HAPReferenceRoot cloneStructureRootReference();
	
	String getStructureType();
	
}
