package com.nosliw.data.core.story.change;

import java.util.ArrayList;
import java.util.List;

public class HAPChangeResult {

	private List<HAPChangeItem> m_extendChanges;
	
	private List<HAPChangeItem> m_revertChanges;

	private Class<?> m_processor;
	
	public HAPChangeResult() {
		this.m_extendChanges = new ArrayList<HAPChangeItem>();
		this.m_revertChanges = new ArrayList<HAPChangeItem>();
	}

	public HAPChangeResult(Class<?> processor) {
		this();
		this.m_processor = processor;
	}

	public Class<?> getProcessor() {  return this.m_processor;  }
	
	public void addExtendChange(HAPChangeItem change) {  this.m_extendChanges.add(change);     }
	public List<HAPChangeItem> getExtendChanges(){    return this.m_extendChanges;     }
	
	public void addRevertChange(HAPChangeItem change) {   this.m_revertChanges.add(change);     }
	public List<HAPChangeItem> getRevertChanges(){   return this.m_revertChanges;    }
	
}
