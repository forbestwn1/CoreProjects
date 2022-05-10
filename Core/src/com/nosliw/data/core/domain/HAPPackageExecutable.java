package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.data.core.resource.HAPResourceIdSimple;

//executable package
public class HAPPackageExecutable {

	//all related entity grouped by complex resource
	private Map<HAPResourceIdSimple, HAPPackageComplexResource> m_complexDomainByResources;

	//main complex resource this package represent
	private HAPResourceIdSimple m_mainResourceId;
	
	//id for target main entity in main complex resouce
	private HAPIdComplexEntityInGlobal m_mainEntityId;
	
}
