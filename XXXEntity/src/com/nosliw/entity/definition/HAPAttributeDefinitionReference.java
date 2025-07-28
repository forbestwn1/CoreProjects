package com.nosliw.entity.definition;

import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;

public class HAPAttributeDefinitionReference extends HAPAttributeDefinitionEntity{

	public HAPAttributeDefinitionReference(String name, HAPEntityDefinitionSegment entityDef, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan) {
		super(name, entityDef, dataTypeMan, entityDefMan, optionsMan);
	}

	@Override
	public HAPAttributeDefinition cloneDefinition(HAPEntityDefinitionSegment entityDef)
	{
		HAPAttributeDefinitionReference out = new HAPAttributeDefinitionReference(this.getName(), entityDef, this.getDataTypeManager(), this.getEntityDefinitionManager(), this.getOptionsManager());
		cloneTo(out);
		return out;
	}
}
