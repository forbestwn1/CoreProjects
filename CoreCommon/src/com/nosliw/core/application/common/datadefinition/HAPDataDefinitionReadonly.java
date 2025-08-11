package com.nosliw.core.application.common.datadefinition;

public class HAPDataDefinitionReadonly extends HAPDataDefinition{

	public HAPDataDefinitionReadonly cloneDataDefinitionWritable() {
		HAPDataDefinitionReadonly out = new HAPDataDefinitionReadonly();
		this.cloneToDataDefinition(out);
		return out;
	}
	
}
