package com.nosliw.data.core.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutableImp;

//executable package
public class HAPPackageExecutable extends HAPExecutableImp{

	public static final String MAINENTITYID = "mainEntityId";
	public static final String COMPLEXRESOURCEPACKAGEGROUP = "complexResourcePackageGroup";
	
	private Set<HAPResourceIdSimple> m_complexResourceBundleGroup;
	
	private HAPIdComplexEntityInGlobal m_rootEntityId;
	
	public HAPPackageExecutable() {
		this.m_complexResourceBundleGroup = new HashSet<HAPResourceIdSimple>();
	}
	
	public Set<HAPResourceIdSimple> getComplexResourceBundleGroup() {     return this.m_complexResourceBundleGroup;      }
	public void addComplexResourceBundleId(HAPResourceIdSimple bundleId) {   this.m_complexResourceBundleGroup.add(bundleId);   }
	
	public HAPIdComplexEntityInGlobal getRootEntityId() {    return this.m_rootEntityId;     }
	
	public void setRootEntityId(HAPIdComplexEntityInGlobal rootEntityId) {    this.m_rootEntityId = rootEntityId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){

	}
}
