package com.nosliw.data.core.domain.entity;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPEmbededDefinitionWithValue extends HAPEmbededDefinition{

	public HAPEmbededDefinitionWithValue() {}
	
	public HAPEmbededDefinitionWithValue(Object value) {
		super(value, null, false);
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EMBEDED_TYPE_DEFINITION_VALUE;  }

	@Override
	public HAPEmbededDefinitionWithValue cloneEmbeded() {
		HAPEmbededDefinitionWithValue out = new HAPEmbededDefinitionWithValue(this.getValue());
		this.cloneToEmbeded(out);
		return out;
	}
}
