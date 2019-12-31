package com.nosliw.servlet.utils.browseresource;

import java.util.ArrayList;
import java.util.List;

public class HAPResourceNodeContainerByType {

	private String m_type;
	
	private List<HAPResourceNode> m_elements;

	public HAPResourceNodeContainerByType(String type) {
		this.m_type = type;
		this.m_elements = new ArrayList<HAPResourceNode>();
	}
	
	public void addElement(HAPResourceNode ele) {
		this.m_elements.add(ele);
	}
	
}
