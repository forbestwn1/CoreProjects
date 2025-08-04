package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.Map;

import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPValueStructure;
import com.nosliw.core.application.common.structure.HAPValueStructureImp;
import com.nosliw.core.application.division.manual.core.a.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBrickValueStructure extends HAPManualDefinitionBrick implements HAPValueStructure{

	public static final String VALUE = "value";

	public HAPManualDefinitionBrickValueStructure() {
		super(HAPManualEnumBrickType.VALUESTRUCTURE_100);
		this.setAttributeValueWithValue(VALUE, new HAPValueStructureImp());
	}

	public HAPValueStructure getValue() {   return (HAPValueStructure)this.getAttributeValueOfValue(VALUE);       }
	public void setValue(HAPValueStructure valueStructureDef) {   this.setAttributeValueWithValue(VALUE, valueStructureDef);     }

	@Override
	public Object getInitValue() {    return this.getValue().getInitValue();  }

	@Override
	public void setInitValue(Object initValue) {   this.getValue().setInitValue(initValue);  }

	@Override
	public HAPRootInStructure addRoot(HAPRootInStructure root) {   return this.getValue().addRoot(root);  }

	@Override
	public Map<String, HAPRootInStructure> getRoots() {  return this.getValue().getRoots();  }
	
}
