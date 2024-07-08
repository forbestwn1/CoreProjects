package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;

public class HAPPluginBrick {

	private HAPIdBrickType m_brickTypeId;
	
	public HAPPluginBrick(HAPIdBrickType brickTypeId) {
		this.m_brickTypeId = brickTypeId;
	}
	
	public HAPIdBrickType getBrickType() {     return this.m_brickTypeId;      }
	
	//what kind of embeded resource to expose
	public List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick){   return new ArrayList<HAPInfoExportResource>();    }

}
