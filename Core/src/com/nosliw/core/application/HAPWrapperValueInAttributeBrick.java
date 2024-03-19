package com.nosliw.core.application;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPWrapperValueInAttributeBrick extends HAPWrapperValueInAttribute implements HAPWithBrick{

	private HAPBrick m_brick;
	
	private HAPIdBrickType m_entityTypeId;
	
	public HAPWrapperValueInAttributeBrick(HAPBrick brick) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK);
		this.m_brick = brick;
		this.m_entityTypeId = this.m_brick.getBrickTypeInfo().getBrickTypeId();
	}
	
	@Override
	public Object getValue() {    return this.getBrick();     }
	
	
	@Override
	public HAPBrick getBrick() {    return this.m_brick;    }

	@Override
	public HAPIdBrickType getBrickTypeId() {   return this.m_entityTypeId;  }

}
