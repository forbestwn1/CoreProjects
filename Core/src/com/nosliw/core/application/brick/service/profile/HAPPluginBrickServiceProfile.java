package com.nosliw.core.application.brick.service.profile;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.division.manual.executable.HAPBrick;
import com.nosliw.core.application.division.manual.executable.HAPInfoBrickType;
import com.nosliw.core.application.division.manual.executable.HAPPluginBrickImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginBrickServiceProfile extends HAPPluginBrickImp{

	public HAPPluginBrickServiceProfile(HAPInfoBrickType brickTypeInfo, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeInfo, HAPBlockServiceProfile.class, runtimeEnv);
	}

	@Override
	public List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick){
		List<HAPInfoExportResource> out = new ArrayList<HAPInfoExportResource>();
		
		HAPInfoExportResource exposeInteractiveInterface = new HAPInfoExportResource(new HAPPath(HAPBlockServiceProfile.INTERFACE));
		exposeInteractiveInterface.setName(HAPBlockServiceProfile.CHILD_INTERFACE);
		out.add(exposeInteractiveInterface);

		return out;
	}
	
	
}
