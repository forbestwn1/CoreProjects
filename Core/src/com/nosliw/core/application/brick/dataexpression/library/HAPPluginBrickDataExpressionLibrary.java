package com.nosliw.core.application.brick.dataexpression.library;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.executable.HAPAttributeInBrick;
import com.nosliw.core.application.division.manual.executable.HAPBrick;
import com.nosliw.core.application.division.manual.executable.HAPInfoBrickType;
import com.nosliw.core.application.division.manual.executable.HAPPluginBrickImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginBrickDataExpressionLibrary extends HAPPluginBrickImp{

	public HAPPluginBrickDataExpressionLibrary(HAPInfoBrickType brickTypeInfo, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeInfo, HAPBlockDataExpressionLibrary.class, runtimeEnv);
	}

	@Override
	public List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick){

		List<HAPInfoExportResource> out = new ArrayList<HAPInfoExportResource>();
		
		HAPBlockDataExpressionLibrary library = (HAPBlockDataExpressionLibrary)brick;
		List<HAPAttributeInBrick> eleAttrs = library.getItems().getElements();
		for(HAPAttributeInBrick eleAttr : eleAttrs) {
			HAPInfoExportResource exposeInteractiveInterface = new HAPInfoExportResource(new HAPPath(HAPBlockDataExpressionLibrary.ITEM).appendSegment(eleAttr.getName()).appendSegment(HAPBlockTaskWrapper.TASK));
			exposeInteractiveInterface.setName(eleAttr.getName());
			out.add(exposeInteractiveInterface);
		}

		return out;
	}
	
}
