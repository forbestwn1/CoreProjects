package com.nosliw.core.application.brick.service.profile;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPInfoExportBrick;
import com.nosliw.core.application.HAPPluginBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;

public class HAPPluginBrickServiceProfile extends HAPPluginBrick{

	public HAPPluginBrickServiceProfile() {
		super(HAPEnumBrickType.SERVICEPROFILE_100);
	}

	@Override
	public List<HAPInfoExportBrick> getExposeResourceInfo(HAPBrick brick){
		List<HAPInfoExportBrick> out = new ArrayList<HAPInfoExportBrick>();
		
		HAPInfoExportBrick exposeInteractiveInterface = new HAPInfoExportBrick(new HAPPath(HAPWithBlockInteractiveTask.TASKINTERFACE));
		exposeInteractiveInterface.setName(HAPBlockServiceProfile.CHILD_INTERFACE);
		out.add(exposeInteractiveInterface);

		return out;
	}
}
