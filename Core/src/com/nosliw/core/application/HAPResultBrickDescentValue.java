package com.nosliw.core.application;

import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResultBrickDescentValue {

	private HAPBrick m_brick;

	private HAPResourceId m_resourceId;
	
	private HAPValueOfDynamic m_dynamicValue;
	
	private Object m_valueObject;
	
	public HAPResultBrickDescentValue(HAPBrick brick) {
		this.m_brick = brick;
	}
	
	public HAPResultBrickDescentValue(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
	}

	public HAPResultBrickDescentValue(HAPValueOfDynamic dynamicValue) {
		this.m_dynamicValue = dynamicValue;
	}

	public HAPResultBrickDescentValue(Object valueObj) {
		this.m_valueObject = valueObj;
	}

	public HAPBrick getBrick() {   return this.m_brick;  }

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }

	public HAPValueOfDynamic getDyanmicValue() {   return this.m_dynamicValue;      }
	
	public Object getValueObject() {   return this.m_valueObject;     }
	
}
