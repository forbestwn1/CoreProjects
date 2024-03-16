package com.nosliw.ui.entity.uicontent;

import com.nosliw.core.application.division.manual.HAPManualEntity;

public class HAPDefinitionEntityComplexUIPage extends HAPDefinitionEntityComplexWithUIContent{

	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexUIPage out = new HAPDefinitionEntityComplexUIPage();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
