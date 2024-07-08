package com.nosliw.core.application.brick.service.profile;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.HAPPluginBrick;

public class HAPPluginBrickServiceProfile extends HAPPluginBrick{

	public HAPPluginBrickServiceProfile() {
		super(HAPEnumBrickType.SERVICEPROFILE_100);
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
