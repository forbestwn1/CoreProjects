package com.nosliw.data.core.domain.entity.uipage;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public abstract class HAPDefinitionEntityComplexWithUIUnit extends HAPDefinitionEntityInDomainComplex{

	static final public String ATTR_UIUNIT = "uiUnit";
	
	public HAPIdEntityInDomain getUIUnit() {    return this.getAttributeValueComplex(ATTR_UIUNIT);        }
	public void setUIUnit(HAPIdEntityInDomain unUnit) {     this.setAttributeValueComplex(ATTR_UIUNIT, unUnit);       }
	public HAPDefinitionEntityComplexUIUnit getUIUnitEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityComplexUIUnit)parserContext.getGlobalDomain().getEntityInfoDefinition(this.getUIUnit()).getEntity();    }
	
}
