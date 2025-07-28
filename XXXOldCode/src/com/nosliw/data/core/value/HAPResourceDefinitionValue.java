package com.nosliw.data.core.value;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPResourceDefinitionValue extends HAPResourceDefinition {

	private HAPValue m_value;
	
	@Override
	public String getResourceType() {  return HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE;  }

	public HAPValue getValue() {    return this.m_value;    }
	
	public void setValue(HAPValue obj) {   this.m_value = obj;    }


}
