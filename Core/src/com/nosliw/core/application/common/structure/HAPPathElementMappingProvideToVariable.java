package com.nosliw.core.application.common.structure;

import java.util.List;

public class HAPPathElementMappingProvideToVariable extends HAPPathElementMapping{

	private List<HAPPathElementMapping> m_providerMappingPaths;
	
	public HAPPathElementMappingProvideToVariable(String path, List<HAPPathElementMapping> providerMappingPaths) {
		super(HAPPathElementMapping.PROVIDE2VARIABLE, path);
		this.m_providerMappingPaths = providerMappingPaths;
	}

	
}
