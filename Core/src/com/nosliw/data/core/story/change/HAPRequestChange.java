package com.nosliw.data.core.story.change;

import java.util.ArrayList;
import java.util.List;

public class HAPRequestChange {

	private List<HAPChangeItem> m_changes;
	
	private boolean m_extend;
	
	public HAPRequestChange() {
		this(true);
	}

	public HAPRequestChange(boolean extend) {
		this.m_changes = new ArrayList<HAPChangeItem>();
		this.m_extend = extend;
	}

	public List<HAPChangeItem> getChanges(){   return this.m_changes;    }
	
	public boolean isExtend() {   return this.m_extend;    }
	public void notExtend() {   this.m_extend = false;    }
	
}
