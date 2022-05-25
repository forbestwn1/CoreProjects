package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutableImp;

//executable package
public class HAPPackageExecutable extends HAPExecutableImp{

	public static final String MAINRESOURCEID = "mainResourceId";
	public static final String MAINENTITYID = "mainEntityId";
	public static final String COMPLEXRESOURCEPACKAGEGROUP = "complexResourcePackageGroup";
	
	private HAPPackageGroupComplexResource m_complexResourceGroupPackage;
	
	//main complex resource this package represent
	private HAPResourceIdSimple m_mainResourceId;
	
	//id for target main entity in main complex resouce
	private HAPIdComplexEntityInGlobal m_mainEntityId;

	public HAPPackageExecutable() {
		this.m_complexResourceGroupPackage = new HAPPackageGroupComplexResource();
	}
	
	public HAPPackageGroupComplexResource getComplexResourcePackageGroup() {     return this.m_complexResourceGroupPackage;      }

	public HAPResourceIdSimple getMainResourceId() {    return this.m_mainResourceId;     }
	
	public HAPIdComplexEntityInGlobal getMainEntityId() {    return this.m_mainEntityId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){

	}
}
