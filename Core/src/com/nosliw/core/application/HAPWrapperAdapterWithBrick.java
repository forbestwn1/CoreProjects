package com.nosliw.core.application;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPWrapperAdapterWithBrick extends HAPWrapperAdapter{

	public static final String BRICK = "brick";

	private HAPBrick m_brick;

	
	public HAPWrapperAdapterWithBrick(HAPBrick brick) {
		this.m_brick = brick;
	}
	
	public HAPBrick getBrick() {   return this.m_brick;     }
	
	@Override
	public String getValueType() {  return HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK;  }
	
}
