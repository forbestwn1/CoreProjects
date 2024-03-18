package com.nosliw.ui.entity.uicontent;

import com.nosliw.core.application.division.manual.HAPManualBrick;

public class HAPDefinitionEntityComplexUIPage extends HAPDefinitionEntityComplexWithUIContent{

	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexUIPage out = new HAPDefinitionEntityComplexUIPage();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
