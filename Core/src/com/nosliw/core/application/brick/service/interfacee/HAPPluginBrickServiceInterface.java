package com.nosliw.core.application.brick.service.interfacee;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.division.manual.executable.HAPBrick;
import com.nosliw.core.application.division.manual.executable.HAPInfoBrickType;
import com.nosliw.core.application.division.manual.executable.HAPPluginBrickImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginBrickServiceInterface extends HAPPluginBrickImp{

	public HAPPluginBrickServiceInterface(HAPInfoBrickType brickTypeInfo, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeInfo, HAPBlockServiceInterface.class, runtimeEnv);
	}

	@Override
	public List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick){
		List<HAPInfoExportResource> out = new ArrayList<HAPInfoExportResource>();
		
		HAPInfoExportResource exposeInteractiveInterface = new HAPInfoExportResource(new HAPPath(HAPBlockServiceInterface.INTERFACE));
		exposeInteractiveInterface.setName(HAPBlockServiceInterface.CHILD_INTERFACE);
		out.add(exposeInteractiveInterface);

		return out;
	}
	
}
