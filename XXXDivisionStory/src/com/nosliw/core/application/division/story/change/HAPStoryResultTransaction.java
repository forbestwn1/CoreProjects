package com.nosliw.core.application.division.story.change;

import java.util.ArrayList;
import java.util.List;

public class HAPStoryResultTransaction {

	private List<HAPStoryChangeItem> m_changes;

	private int m_oldIdIndex;
	
	public HAPStoryResultTransaction() {
		this.m_changes = new ArrayList<HAPStoryChangeItem>();
	}
	
	public List<HAPStoryChangeItem> getChanges(){   return this.m_changes;    }
	public void addChanges(List<HAPStoryChangeItem> changes) {    this.m_changes.addAll(changes);      }
	public void addChange(HAPStoryChangeItem change) {    this.m_changes.add(change);    }
	
	public int getOldIdIndex() {   return this.m_oldIdIndex;   }
	public void setOldIdIndex(int idIndex) {    this.m_oldIdIndex = idIndex;      }
	
}
