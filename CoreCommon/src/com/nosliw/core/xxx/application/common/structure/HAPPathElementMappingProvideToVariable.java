package com.nosliw.core.xxx.application.common.structure;

import java.util.List;

import com.nosliw.core.application.common.structure.reference.HAPPathElementMapping;

public class HAPPathElementMappingProvideToVariable extends HAPPathElementMapping{

	private List<HAPPathElementMapping> m_providerMappingPaths;
	
	public HAPPathElementMappingProvideToVariable(String path, List<HAPPathElementMapping> providerMappingPaths) {
		super(HAPPathElementMapping.PROVIDE2VARIABLE, path);
		this.m_providerMappingPaths = providerMappingPaths;
	}

	
}
