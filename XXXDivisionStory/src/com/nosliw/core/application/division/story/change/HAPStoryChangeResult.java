package com.nosliw.core.application.division.story.change;

import java.util.ArrayList;
import java.util.List;

public class HAPStoryChangeResult {

	private List<HAPStoryChangeItem> m_extendChanges;
	
	private List<HAPStoryChangeItem> m_revertChanges;

	private Class<?> m_processor;
	
	public HAPStoryChangeResult() {
		this.m_extendChanges = new ArrayList<HAPStoryChangeItem>();
		this.m_revertChanges = new ArrayList<HAPStoryChangeItem>();
	}

	public HAPStoryChangeResult(Class<?> processor) {
		this();
		this.m_processor = processor;
	}

	public Class<?> getProcessor() {  return this.m_processor;  }
	
	public void addExtendChange(HAPStoryChangeItem change) {  this.m_extendChanges.add(change);     }
	public List<HAPStoryChangeItem> getExtendChanges(){    return this.m_extendChanges;     }
	
	public void addRevertChange(HAPStoryChangeItem change) {   this.m_revertChanges.add(change);     }
	public List<HAPStoryChangeItem> getRevertChanges(){   return this.m_revertChanges;    }
	
}
