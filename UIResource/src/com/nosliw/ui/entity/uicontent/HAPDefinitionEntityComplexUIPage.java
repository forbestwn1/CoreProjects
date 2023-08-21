package com.nosliw.ui.entity.uicontent;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

public class HAPDefinitionEntityComplexUIPage extends HAPDefinitionEntityComplexWithUIContent{

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexUIPage out = new HAPDefinitionEntityComplexUIPage();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
