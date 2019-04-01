package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.List;

public class HAPResourceUtility {

	
	public static List<HAPResourceDependent> buildResourceDependentFromResourceId(List<HAPResourceId> ids){
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		for(HAPResourceId id : ids) 	out.add(new HAPResourceDependent(id));
		return out;
	}
}
