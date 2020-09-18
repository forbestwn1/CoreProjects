package com.nosliw.data.core.story.change;

import java.util.List;

public class HAPResultTransaction {

	private List<HAPChangeItem> m_changes;
	
	public List<HAPChangeItem> getChanges(){   return this.m_changes;    }
	public void addChanges(List<HAPChangeItem> changes) {    this.m_changes.addAll(changes);      }
	
}
