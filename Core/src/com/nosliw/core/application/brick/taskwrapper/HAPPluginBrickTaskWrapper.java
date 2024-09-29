package com.nosliw.core.application.brick.taskwrapper;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.HAPPluginBrick;

public class HAPPluginBrickTaskWrapper extends HAPPluginBrick{

	public HAPPluginBrickTaskWrapper() {
		super(HAPEnumBrickType.TASKWRAPPER_100);
	}

	@Override
	public List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick){
		List<HAPInfoExportResource> out = new ArrayList<HAPInfoExportResource>();
		HAPInfoExportResource exposeTaskResourceInfo = new HAPInfoExportResource(new HAPPath(HAPBlockTaskWrapper.TASK));
		exposeTaskResourceInfo.setName(HAPBlockTaskWrapper.CHILD_TASK);
		out.add(exposeTaskResourceInfo);
		return out;
	}
	
}
