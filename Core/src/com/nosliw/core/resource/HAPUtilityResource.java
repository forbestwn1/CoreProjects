package com.nosliw.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPResourceDataBrick;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.system.HAPSystemUtility;

public class HAPUtilityResource {

	public static HAPResource getResource(HAPResourceId resourceId, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		return resourceMan.getResources(Lists.asList(resourceId, new HAPResourceId[0]), runtimeInfo).getLoadedResource(resourceId);
	}

	public static HAPBrick getResourceDataBrick(HAPResourceId resourceId, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPResourceDataBrick brickResourceData = (HAPResourceDataBrick)resourceMan.getResources(Lists.asList(resourceId, new HAPResourceId[0]), runtimeInfo).getLoadedResource(resourceId).getResourceData();
		return brickResourceData.getBrick();
	}
	public static HAPResourceDataBrick getResourceData(HAPResourceId resourceId, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPResourceDataBrick brickResourceData = (HAPResourceDataBrick)resourceMan.getResources(Lists.asList(resourceId, new HAPResourceId[0]), runtimeInfo).getLoadedResource(resourceId).getResourceData();
		return brickResourceData;
	}

	public static Map<String, Object> buildResourceLoadPattern(HAPResourceId resourceId, Map<String, Object> info) {
		if(info==null) {
			info = new LinkedHashMap<String, Object>();
		}
		if(isLoadResoureByFile(resourceId.getResourceTypeId().getResourceType())) {
			info.put(HAPConfigureResource.RESOURCE_LOADPATTERN, HAPConfigureResource.RESOURCE_LOADPATTERN_FILE);
		}
		return info;
	}

	public final static String LOADRESOURCEBYFILE_MODE_NEVER = "never";
	public final static String LOADRESOURCEBYFILE_MODE_ALWAYS = "always";
	public final static String LOADRESOURCEBYFILE_MODE_DEPENDS = "depends";
	

	private final static Set<String> loadResourceByFile = HAPSystemUtility.getLoadResoureByFile();
	public static boolean isLoadResoureByFile(String resourceType) {
		String mode = HAPSystemUtility.getLoadResourceByFileMode();
		if(mode==null) {
			mode = LOADRESOURCEBYFILE_MODE_DEPENDS;
		}
		if(LOADRESOURCEBYFILE_MODE_NEVER.equals(resourceType)) {
			return false;
		}
		if(LOADRESOURCEBYFILE_MODE_ALWAYS.equals(resourceType)) {
			return true;
		}
		return loadResourceByFile.contains(resourceType);
	}
	
}
