package com.nosliw.core.application.brick.service.interfacee;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.HAPPluginBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;

public class HAPPluginBrickServiceInterface extends HAPPluginBrick{

	public HAPPluginBrickServiceInterface() {
		super(HAPEnumBrickType.SERVICEINTERFACE_100);
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
