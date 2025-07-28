package com.nosliw.core.application.common.structure;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPReferenceRootInStrucutre extends HAPSerializable{

	@HAPAttribute
	public static final String STRUCTURETYPE = "structureType";

	HAPReferenceRootInStrucutre cloneStructureRootReference();
	
	String getStructureType();
	
}
