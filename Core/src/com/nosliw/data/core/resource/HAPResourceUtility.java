package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

public class HAPResourceUtility {

	private final static Set<String> loadResourceByFile = HAPSystemUtility.getLoadResoureByFile();
	public static boolean isLoadResoureByFile(String resourceType) {
		return loadResourceByFile.contains(resourceType);
	}
	
	public static List<HAPResourceDependent> buildResourceDependentFromResourceId(List<HAPResourceId> ids){
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		for(HAPResourceId id : ids) 	out.add(new HAPResourceDependent(id));
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
