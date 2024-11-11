package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializable;

public interface HAPValueStructureDefinition extends HAPSerializable{

	public static final String ROOT = "root";

	public static final String INITVALUE = "initValue";
	
	Object getInitValue(); 
	void setInitValue(Object initValue);
	
	HAPRootInStructure addRoot(HAPRootInStructure root);
	Map<String, HAPRootInStructure> getRoots();

}
