package com.nosliw.ui.entity.uicontent;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public abstract class HAPDefinitionEntityComplexWithUIContent extends HAPDefinitionEntityInDomainComplex{

	public HAPIdEntityInDomain getContent() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplexWithUIContent.UICONTENT);        }
	public void setContent(HAPIdEntityInDomain unUnit) {     this.setAttributeValueComplex(HAPExecutableEntityComplexWithUIContent.UICONTENT, unUnit);       }
	public HAPDefinitionEntityComplexUIContent getContent(HAPContextParser parserContext) {		return (HAPDefinitionEntityComplexUIContent)this.getAttributeValueEntity(HAPExecutableEntityComplexWithUIContent.UICONTENT, parserContext);  }
	
}
