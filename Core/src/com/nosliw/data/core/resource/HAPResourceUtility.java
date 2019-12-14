package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

public class HAPResourceUtility {

	public final static String LOADRESOURCEBYFILE_MODE_NEVER = "never";
	public final static String LOADRESOURCEBYFILE_MODE_ALWAYS = "always";
	public final static String LOADRESOURCEBYFILE_MODE_DEPENDS = "depends";
	
	private final static Set<String> loadResourceByFile = HAPSystemUtility.getLoadResoureByFile();
	public static boolean isLoadResoureByFile(String resourceType) {
		String mode = HAPSystemUtility.getLoadResourceByFileMode();
		if(mode==null)  mode = LOADRESOURCEBYFILE_MODE_DEPENDS;
		if(LOADRESOURCEBYFILE_MODE_NEVER.equals(resourceType))  return false;
		if(LOADRESOURCEBYFILE_MODE_ALWAYS.equals(resourceType))  return true;
		return loadResourceByFile.contains(resourceType);
	}
	
	public static List<HAPResourceDependency> buildResourceDependentFromResourceId(List<HAPResourceId> ids){
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(HAPResourceId id : ids) 	out.add(new HAPResourceDependency(id));
		return out;
	}

	
	public static Map<String, Object> buildResourceLoadPattern(HAPResourceId resourceId, Map<String, Object> info) {
		if(info==null)   info = new LinkedHashMap<String, Object>();
		if(isLoadResoureByFile(resourceId.getType())) {
			info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		}
		return info;
	}

}
