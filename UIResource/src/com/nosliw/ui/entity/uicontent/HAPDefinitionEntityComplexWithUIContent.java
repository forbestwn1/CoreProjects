package com.nosliw.ui.entity.uicontent;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public abstract class HAPDefinitionEntityComplexWithUIContent extends HAPDefinitionEntityInDomainComplex{

	static final public String ATTR_CONTENT = "content";
	
	public HAPIdEntityInDomain getContent() {    return this.getAttributeValueEntityId(ATTR_CONTENT);        }
	public void setContent(HAPIdEntityInDomain unUnit) {     this.setAttributeValueComplex(ATTR_CONTENT, unUnit);       }
	public HAPDefinitionEntityComplexUIContent getContent(HAPContextParser parserContext) {		return (HAPDefinitionEntityComplexUIContent)this.getAttributeValueEntity(ATTR_CONTENT, parserContext);  }
	
}
