package com.nosliw.core.application;

public class HAPResultBrick {

	private HAPBrick m_brick;

	private HAPReferenceBrickGlobal m_globalRef;
	
	public HAPResultBrick(HAPBrick brick) {
		this.m_brick = brick;
	}
	
	public HAPResultBrick(HAPReferenceBrickGlobal globalRef) {
		this.m_globalRef = globalRef;
	}
	
	public boolean isInternalBrick() {    return this.m_brick!=null;    }
	
	public HAPBrick getBrick() {   return this.m_brick;  }
}
