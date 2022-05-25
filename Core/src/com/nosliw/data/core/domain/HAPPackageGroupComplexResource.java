package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPPackageGroupComplexResource {

	//all related entity grouped by complex resource
	private Map<HAPResourceIdSimple, HAPPackageComplexResource> m_complexDomainByResources;

	public HAPPackageGroupComplexResource() {
		this.m_complexDomainByResources = new LinkedHashMap<HAPResourceIdSimple, HAPPackageComplexResource>();
	}

	public Set<HAPResourceIdSimple> getComplexResourceIds(){    return this.m_complexDomainByResources.keySet();     }
	
	public HAPPackageComplexResource getComplexResourcePackage(HAPResourceIdSimple resourceId) {    return this.m_complexDomainByResources.get(resourceId);    }

	public void addComplexResourcePackage(HAPPackageComplexResource complexResourcePackage) {     this.m_complexDomainByResources.put(complexResourcePackage.getRootResourceId(), complexResourcePackage);     }

	
}
