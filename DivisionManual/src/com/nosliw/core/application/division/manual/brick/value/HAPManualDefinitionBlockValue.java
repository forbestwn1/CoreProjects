package com.nosliw.core.application.division.manual.brick.value;

import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.value.HAPBlockValue;

public class HAPManualDefinitionBlockValue extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockValue() {
		super(HAPEnumBrickType.VALUE_100);
	}

	public Object getValue() {    return this.getAttributeValueOfValue(HAPBlockValue.VALUE);     }

	public void setValue(Object obj) {    this.setAttributeValueWithValue(HAPBlockValue.VALUE, obj);      }
	
}
