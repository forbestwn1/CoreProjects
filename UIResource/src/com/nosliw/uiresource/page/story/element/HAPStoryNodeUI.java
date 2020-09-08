package com.nosliw.uiresource.page.story.element;

import com.nosliw.data.core.story.HAPStoryNodeImp;

public class HAPStoryNodeUI extends HAPStoryNodeImp{

	private HAPUIDataStructureInfo m_dataStructureInfo;
	
	public HAPStoryNodeUI(String type) {
		super(type);
		this.m_dataStructureInfo = new HAPUIDataStructureInfo();
	}
	
	public HAPUIDataStructureInfo getDataStructureInfo() {
		return this.m_dataStructureInfo;
	}
	
}
