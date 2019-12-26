package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoWritableImp;

public class HAPWithExternalMappingEntityInfoImp extends HAPEntityInfoWritableImp implements HAPWithExternalMapping{

	private HAPDefinitionExternalMapping m_exteranlMapping;
	
	public HAPWithExternalMappingEntityInfoImp() {
		this.m_exteranlMapping = new HAPDefinitionExternalMapping();
	}

	@Override
	public HAPDefinitionExternalMapping getExternalMapping() {		return this.m_exteranlMapping;	}

	@Override
	public Map<String, HAPDefinitionExternalMappingEle> getElementsByType(String type) {
		return this.m_exteranlMapping.getMappingByType(type);
	}

	@Override
	public void mergeBy(HAPWithExternalMapping parent, String mode) {
		this.m_exteranlMapping.merge(parent.getExternalMapping(), mode);
	}
	
}
