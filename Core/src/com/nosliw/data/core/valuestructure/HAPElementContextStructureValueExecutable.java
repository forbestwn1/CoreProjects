package com.nosliw.data.core.valuestructure;

import com.nosliw.data.core.structure.HAPRootStructure;

public class HAPElementContextStructureValueExecutable {

	private HAPRootStructure m_contextRoot;
	
	private String m_categary;
	
	public HAPElementContextStructureValueExecutable(HAPRootStructure contextRoot, String categary) {
		this.m_contextRoot = contextRoot;
		this.m_categary = categary;
	}
	
	public HAPElementContextStructureValueExecutable(HAPRootStructure contextRoot) {
		this(contextRoot, null);
	}
	
	public String getLocalId() {   return this.m_contextRoot.getLocalId();    }
}
