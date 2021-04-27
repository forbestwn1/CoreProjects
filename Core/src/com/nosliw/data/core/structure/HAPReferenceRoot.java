package com.nosliw.data.core.structure;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPReferenceRoot extends HAPSerializable{

	@HAPAttribute
	public static final String STRUCTURETYPE = "structureType";

	HAPReferenceRoot cloneStructureRootReference();
	
	String getStructureType();
	
}
