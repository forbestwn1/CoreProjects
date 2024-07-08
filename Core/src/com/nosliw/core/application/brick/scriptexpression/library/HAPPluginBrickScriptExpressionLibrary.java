package com.nosliw.core.application.brick.scriptexpression.library;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPInfoExportResource;
import com.nosliw.core.application.HAPPluginBrick;
import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;

public class HAPPluginBrickScriptExpressionLibrary extends HAPPluginBrick{

	public HAPPluginBrickScriptExpressionLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100);
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
