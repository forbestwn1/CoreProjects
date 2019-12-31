package com.nosliw.servlet.utils.browseresource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceNode {

	private HAPResourceId m_resourceId;
	
	private String m_url;
	
	private List<HAPResourceNodeContainerByType> m_children;
	
	public HAPResourceNode(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
		this.m_children = new ArrayList<HAPResourceNodeContainerByType>();
	}
	
	public void setUrl(String url) {
		this.m_url = url;
	}
	
	public void addChild(HAPResourceNodeContainerByType childByType) {
		this.m_children.add(childByType);
	}
	
}
