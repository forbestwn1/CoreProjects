package com.nosliw.data.core.valuestructure;

import com.nosliw.data.core.structure.HAPRoot;

public class HAPElementContextStructureValueExecutable {

	private HAPRoot m_contextRoot;
	
	private String m_categary;
	
	public HAPElementContextStructureValueExecutable(HAPRoot contextRoot, String categary) {
		this.m_contextRoot = contextRoot;
		this.m_categary = categary;
	}
	
	public HAPElementContextStructureValueExecutable(HAPRoot contextRoot) {
		this(contextRoot, null);
	}
	
	public String getLocalId() {   return this.m_contextRoot.getLocalId();    }
}
