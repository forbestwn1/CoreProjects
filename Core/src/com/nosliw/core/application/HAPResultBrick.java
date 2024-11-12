package com.nosliw.core.application;

import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResultBrick {

	private HAPBrick m_brick;

	private HAPResourceId m_resourceId;
	
	public HAPResultBrick(HAPBrick brick) {
		this.m_brick = brick;
	}
	
	public HAPResultBrick(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
	}
	
	public boolean isInternalBrick() {    return this.m_resourceId==null;    }
	
	public HAPBrick getBrick() {   return this.m_brick;  }
	public void setBrick(HAPBrick brick) {    this.m_brick = brick;      }
	
	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	
}
