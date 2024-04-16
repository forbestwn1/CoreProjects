package com.nosliw.core.application;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPWrapperValueInAttributeBrick extends HAPWrapperValueInAttribute implements HAPWithBrick{

	private HAPBrick m_brick;
	
	public HAPWrapperValueInAttributeBrick(HAPBrick brick) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK);
		this.m_brick = brick;
	}
	
	@Override
	public Object getValue() {    return this.getBrick();     }
	
	
	@Override
	public HAPBrick getBrick() {    return this.m_brick;    }

}
