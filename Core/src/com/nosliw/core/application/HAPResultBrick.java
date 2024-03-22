package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public class HAPResultBrick {

	private HAPBrick m_brick;
	
	private HAPBundle m_bundle;
	private HAPPath m_pathFromRoot;
	
	public HAPResultBrick(HAPBrick brick) {
		this.m_brick = brick;
	}
	
	public HAPResultBrick(HAPBundle bundle, HAPPath pathFromRoot) {
		this.m_bundle = bundle;
		this.m_pathFromRoot = pathFromRoot;
	}
	
	public boolean isInternalBrick() {    return this.m_brick!=null;    }
	
	public HAPBrick getBrick() {
		if(this.m_brick!=null) {
			return this.m_brick;
		} else {
			return HAPUtilityBrick.getDescdentBrick(this.m_bundle.getBrickWrapper(), m_pathFromRoot);
		}
	}
}
