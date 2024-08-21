package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.Map;

import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinitionImp;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;

public class HAPManualDefinitionBrickValueStructure extends HAPManualDefinitionBrick implements HAPValueStructureDefinition{

	public static final String VALUE = "value";

	public HAPManualDefinitionBrickValueStructure() {
		super(HAPManualEnumBrickType.VALUESTRUCTURE_100);
		this.setAttributeWithValueValue(VALUE, new HAPValueStructureDefinitionImp());
	}

	public HAPValueStructureDefinition getValue() {   return (HAPValueStructureDefinition)this.getAttributeValueWithValue(VALUE);       }
	public void setValue(HAPValueStructureDefinition valueStructureDef) {   this.setAttributeWithValueValue(VALUE, valueStructureDef);     }

	@Override
	public Object getInitValue() {    return this.getValue().getInitValue();  }

	@Override
	public void setInitValue(Object initValue) {   this.getValue().setInitValue(initValue);  }

	@Override
	public HAPRootInValueStructure addRoot(HAPRootInValueStructure root) {   return this.getValue().addRoot(root);  }

	@Override
	public Map<String, HAPRootInValueStructure> getRoots() {  return this.getValue().getRoots();  }
	
}
