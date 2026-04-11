package com.nosliw.core.application.brick.expression.dataexpression.library;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPInfoExportBrick;
import com.nosliw.core.application.HAPPluginBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;

public class HAPPluginBrickDataExpressionLibrary extends HAPPluginBrick{

	public HAPPluginBrickDataExpressionLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100);
	}

	@Override
	public List<HAPInfoExportBrick> getExposeResourceInfo(HAPBrick brick){

		List<HAPInfoExportBrick> out = new ArrayList<HAPInfoExportBrick>();
		
		HAPBlockDataExpressionLibrary library = (HAPBlockDataExpressionLibrary)brick;
		HAPBrickContainer containerBrick =  library.getItems();
		List<HAPAttributeInBrick> eleAttrs = containerBrick.getElements();
		for(HAPAttributeInBrick eleAttr : eleAttrs) {
			HAPInfoExportBrick exposeInteractiveInterface = new HAPInfoExportBrick(new HAPPath(HAPBlockDataExpressionLibrary.ITEM).appendSegment(eleAttr.getName()).appendSegment(HAPBlockTaskWrapper.TASK));
			exposeInteractiveInterface.setName(eleAttr.getName());
			out.add(exposeInteractiveInterface);
		}

		return out;
	}
	
}
