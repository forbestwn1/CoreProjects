package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.data.core.runtime.HAPExecutableImp;

//executable package
public class HAPPackageExecutable extends HAPExecutableImp{

	public static final String MAINRESOURCEID = "mainResourceId";
	public static final String MAINENTITYID = "mainEntityId";
	public static final String COMPLEXRESOURCEPACKAGEGROUP = "complexResourcePackageGroup";
	
	private HAPBundleGroupComplexResource m_complexResourceBundleGroup;
	
	private HAPIdComplexEntityInPackage m_rootEntityId;
	
	public HAPPackageExecutable() {
		this.m_complexResourceBundleGroup = new HAPBundleGroupComplexResource();
	}
	
	public HAPBundleGroupComplexResource getComplexResourcePackageGroup() {     return this.m_complexResourceBundleGroup;      }

	public HAPIdComplexEntityInPackage getRootEntityId() {    return this.m_rootEntityId;     }
	
	public void setRootEntityId(HAPIdComplexEntityInPackage rootEntityId) {    this.m_rootEntityId = rootEntityId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){

	}
}
