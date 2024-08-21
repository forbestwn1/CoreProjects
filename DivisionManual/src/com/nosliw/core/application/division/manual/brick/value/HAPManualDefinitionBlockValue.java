package com.nosliw.core.application.division.manual.brick.value;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.value.HAPBlockValue;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockValue extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockValue() {
		super(HAPEnumBrickType.VALUE_100);
	}

	public Object getValue() {    return this.getAttributeValueWithValue(HAPBlockValue.VALUE);     }

	public void setValue(Object obj) {    this.setAttributeWithValueValue(HAPBlockValue.VALUE, obj);      }
	
}
