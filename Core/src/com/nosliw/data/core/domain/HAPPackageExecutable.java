package com.nosliw.data.core.domain;

import com.nosliw.data.core.resource.HAPResourceIdSimple;

//executable package
public class HAPPackageExecutable {

	private HAPPackageGroupComplexResource m_complexResourceGroupPackage;
	
	//main complex resource this package represent
	private HAPResourceIdSimple m_mainResourceId;
	
	//id for target main entity in main complex resouce
	private HAPIdComplexEntityInGlobal m_mainEntityId;

	public HAPPackageExecutable() {
		this.m_complexResourceGroupPackage = new HAPPackageGroupComplexResource();
	}
	
	public HAPPackageGroupComplexResource getComplexResourcePackageGroup() {     return this.m_complexResourceGroupPackage;      }
}
