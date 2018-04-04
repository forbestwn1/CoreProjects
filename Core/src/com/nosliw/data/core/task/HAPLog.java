package com.nosliw.data.core.task;

import java.util.ArrayList;
import java.util.List;

public class HAPLog {

	private List<HAPLog> m_children;
	
	public HAPLog() {
		this.m_children = new ArrayList<HAPLog>();
	}

	public void addChild(HAPLog child) {	this.m_children.add(child);	}
	
}
