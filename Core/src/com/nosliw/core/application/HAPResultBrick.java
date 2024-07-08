package com.nosliw.core.application;

import com.nosliw.core.application.division.manual.executable.HAPBrick;
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
	
	public boolean isInternalBrick() {    return this.m_brick!=null;    }
	
	public HAPBrick getInternalBrick() {   return this.m_brick;  }
	
	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	
}
