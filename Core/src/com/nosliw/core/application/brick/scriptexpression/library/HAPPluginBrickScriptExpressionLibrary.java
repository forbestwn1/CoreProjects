package com.nosliw.core.application.brick.scriptexpression.library;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPInfoBrickType;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.HAPPluginBrickImp;
import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginBrickScriptExpressionLibrary extends HAPPluginBrickImp{

	public HAPPluginBrickScriptExpressionLibrary(HAPInfoBrickType brickTypeInfo, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeInfo, HAPBlockScriptExpressionLibrary.class, runtimeEnv);
	}

	@Override
	public List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick){

		List<HAPInfoExportResource> out = new ArrayList<HAPInfoExportResource>();
		
		HAPBlockScriptExpressionLibrary library = (HAPBlockScriptExpressionLibrary)brick;
		List<HAPAttributeInBrick> eleAttrs = library.getItems().getElements();
		for(HAPAttributeInBrick eleAttr : eleAttrs) {
			HAPInfoExportResource exposeInteractiveInterface = new HAPInfoExportResource(new HAPPath(HAPBlockScriptExpressionLibrary.ITEM).appendSegment(eleAttr.getName()).appendSegment(HAPBlockTaskWrapper.TASK));
			exposeInteractiveInterface.setName(eleAttr.getName());
			out.add(exposeInteractiveInterface);
		}

		return out;
	}
	
}
