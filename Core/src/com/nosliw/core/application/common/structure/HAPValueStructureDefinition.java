package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;

public interface HAPValueStructureDefinition {

	public static final String ROOT = "root";

	public static final String INITVALUE = "initValue";
	
	Object getInitValue(); 
	void setInitValue(Object initValue);
	
	HAPRootInValueStructure addRoot(HAPRootInValueStructure root);
	Map<String, HAPRootInValueStructure> getRoots();

}
