package com.nosliw.ui.entity.uicontent;

import com.nosliw.core.xxx.application1.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public abstract class HAPDefinitionEntityComplexWithUIContent extends HAPManualBrickComplex{

	public HAPIdEntityInDomain getContent() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplexWithUIContent.UICONTENT);        }
	public void setContent(HAPIdEntityInDomain unUnit) {     this.setAttributeValueComplex(HAPExecutableEntityComplexWithUIContent.UICONTENT, unUnit);       }
	public HAPDefinitionEntityComplexUIContent getContent(HAPContextParser parserContext) {		return (HAPDefinitionEntityComplexUIContent)this.getAttributeValueEntity(HAPExecutableEntityComplexWithUIContent.UICONTENT, parserContext);  }
	
}
