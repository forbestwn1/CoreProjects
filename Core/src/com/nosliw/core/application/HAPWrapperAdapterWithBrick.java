package com.nosliw.core.application;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPWrapperAdapterWithBrick extends HAPWrapperAdapter{

	public static final String BRICK = "brick";

	private HAPAdapter m_adapter;

	
	public HAPWrapperAdapterWithBrick(HAPAdapter adapter) {
		this.m_adapter = adapter;
	}
	
	@Override
	public String getValueType() {  return HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK;  }
	
	public HAPAdapter getAdapter() {   return this.m_adapter;     }
	
}
