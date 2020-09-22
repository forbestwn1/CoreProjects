package com.nosliw.data.core.story.change;

import java.util.ArrayList;
import java.util.List;

public class HAPResultTransaction {

	private List<HAPChangeItem> m_changes;
	
	public HAPResultTransaction() {
		this.m_changes = new ArrayList<HAPChangeItem>();
	}
	
	public List<HAPChangeItem> getChanges(){   return this.m_changes;    }
	public void addChanges(List<HAPChangeItem> changes) {    this.m_changes.addAll(changes);      }
	
}
