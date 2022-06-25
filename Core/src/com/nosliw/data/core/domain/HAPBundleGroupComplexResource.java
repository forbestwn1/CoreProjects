package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPBundleGroupComplexResource {

	//all related entity grouped by complex resource
	private Map<HAPResourceIdSimple, HAPExecutableBundleComplexResource> m_bundleByResources;

	public HAPBundleGroupComplexResource() {
		this.m_bundleByResources = new LinkedHashMap<HAPResourceIdSimple, HAPExecutableBundleComplexResource>();
	}

	public Set<HAPResourceIdSimple> getBundleResourceIds(){    return this.m_bundleByResources.keySet();     }
	
	public HAPExecutableBundleComplexResource getBundle(HAPResourceIdSimple resourceId) {    return this.m_bundleByResources.get(resourceId);    }

	public void addBundle(HAPExecutableBundleComplexResource bundle) {     this.m_bundleByResources.put(bundle.getRootResourceId(), bundle);     }

	
}
