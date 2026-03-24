package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;

public class HAPPluginBrick {

	private HAPIdBrickType m_brickTypeId;
	
	private HAPInfoBrickType m_brickTypeInfo;
	
	public HAPPluginBrick(HAPIdBrickType brickTypeId) {
		this(brickTypeId, null);
	}
	
	public HAPPluginBrick(HAPIdBrickType brickTypeId, HAPInfoBrickType brickTypeInfo) {
		this.m_brickTypeId = brickTypeId;
		this.m_brickTypeInfo = brickTypeInfo;
		if(this.m_brickTypeInfo==null) {
			this.m_brickTypeInfo = new HAPInfoBrickType();
		}
	}
	
	public HAPIdBrickType getBrickType() {     return this.m_brickTypeId;      }
	
	public HAPInfoBrickType getBrickTypeInfo() {    return this.m_brickTypeInfo;     }
	
	//what kind of embeded resource to expose
	public List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick){   return new ArrayList<HAPInfoExportResource>();    }

}
