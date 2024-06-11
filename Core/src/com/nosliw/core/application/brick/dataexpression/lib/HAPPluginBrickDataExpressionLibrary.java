package com.nosliw.core.application.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPInfoBrickType;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.HAPPluginBrickImp;
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
			HAPInfoExportResource exposeInteractiveInterface = new HAPInfoExportResource(new HAPPath(HAPBlockDataExpressionLibrary.ITEM).appendSegment(eleAttr.getName()));
			exposeInteractiveInterface.setName(eleAttr.getName());
			out.add(exposeInteractiveInterface);
		}

		return out;
	}
	
}
