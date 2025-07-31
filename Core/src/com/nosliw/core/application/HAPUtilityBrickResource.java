package com.nosliw.core.application;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPUtilityResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPUtilityBrickResource {

	public static HAPResourceDataBrick getExportResourceData(HAPBundle bundle, String name, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		if(name==null) {
			name = HAPConstantShared.NAME_DEFAULT;
		}
		HAPInfoExportResource exportInfo = null;
		for(HAPInfoExportResource ei : bundle.getExportResourceInfos()) {
			if(name.equals(ei.getName())) {
				exportInfo = ei;
				break;
			}
		}
		
		HAPResourceDataBrick out = null;
		HAPResultBrickDescentValue brickResult = HAPUtilityBrick.getDescdentBrickResult(bundle, exportInfo.getPathFromRoot(), HAPConstantShared.NAME_ROOTBRICK_MAIN);
		if(brickResult.getBrick()!=null) {
			Map<String, HAPBrick> suportBricks = new LinkedHashMap<String, HAPBrick>();
			Map<String, HAPWrapperBrickRoot> branches = bundle.getBranchBrickWrappers();
			for(String n : branches.keySet()) {
				suportBricks.put(n, branches.get(n).getBrick());
			}
			
			out = new HAPResourceDataBrick(brickResult.getBrick(), suportBricks, bundle.getValueStructureDomain());
		}
		else {
			out = (HAPResourceDataBrick)HAPUtilityResource.getResource(brickResult.getResourceId(), resourceMan, runtimeInfo).getResourceData();
		}
		return out;
	}

}
