package com.nosliw.data.core.domain.entity.value;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;

public class HAPDefinitionEntityValue extends HAPDefinitionEntityInDomainSimple{

	public static final String ATTR_VALUE = "value";
	
	public void setValue(Object value) {    this.setAttributeValueObject(ATTR_VALUE, value);    }
	public Object getValue() {   return this.getAttributeValue(ATTR_VALUE);     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityValue out = new HAPDefinitionEntityValue();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
