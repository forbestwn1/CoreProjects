package com.nosliw.data.core.resource.external;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPWithExternalMappingImp extends HAPSerializableImp implements HAPWithExternalMapping{

	private HAPDefinitionExternalMapping m_exteranlMapping;
	
	public HAPWithExternalMappingImp() {
		this.m_exteranlMapping = new HAPDefinitionExternalMapping();
	}

	@Override
	public HAPDefinitionExternalMapping getExternalMapping() {		return this.m_exteranlMapping;	}

	
	@Override
	public Map<String, HAPDefinitionExternalMappingEle> getElementsByType(String type) {
		return this.m_exteranlMapping.getMappingByType(type);
	}

	@Override
	public void mergeBy(HAPWithExternalMapping parent) {
		this.m_exteranlMapping.merge(parent.getExternalMapping());
	}
	
}
