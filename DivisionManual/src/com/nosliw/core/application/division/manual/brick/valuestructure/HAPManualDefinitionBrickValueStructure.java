package com.nosliw.core.application.division.manual.brick.valuestructure;

import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;

public class HAPManualDefinitionBrickValueStructure extends HAPManualDefinitionBrickBlockSimple{

	public static final String VALUE = "value";

	public HAPManualDefinitionBrickValueStructure() {
		super(HAPManualEnumBrickType.VALUESTRUCTURE_100);
		this.setAttributeWithValueValue(VALUE, new HAPValueStructureDefinition());
	}

	public HAPValueStructureDefinition getValue() {   return (HAPValueStructureDefinition)this.getAttributeValueWithValue(VALUE);       }
	public void setValue(HAPValueStructureDefinition valueStructureDef) {   this.setAttributeWithValueValue(VALUE, valueStructureDef);     }
	
}
