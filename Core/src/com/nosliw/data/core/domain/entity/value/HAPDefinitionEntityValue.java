package com.nosliw.data.core.domain.entity.value;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickSimple;

public class HAPDefinitionEntityValue extends HAPManualBrickSimple{

	public static final String ATTR_VALUE = "value";
	
	public void setValue(Object value) {    this.setAttributeValueObject(ATTR_VALUE, value);    }
	public Object getValue() {   return this.getAttributeValue(ATTR_VALUE);     }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityValue out = new HAPDefinitionEntityValue();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
