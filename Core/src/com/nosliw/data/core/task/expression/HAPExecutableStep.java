package com.nosliw.data.core.task.expression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPExecutableTask;

public abstract class HAPExecutableStep  implements HAPExecutable{

	private int m_index;
	
	private String m_name;
	
	public HAPExecutableStep(int index, String name) {
		this.m_index = index;
		this.m_name = name;
	}
	
	public int getIndex() { return this.m_index;  }
	public String getName() {   return this.m_name;   }
	
	public abstract HAPDataTypeCriteria getExitDataTypeCriteria();
	
	public abstract void updateReferencedExecute(Map<String, HAPExecutableTask> references);
	
	public abstract List<HAPResourceId> discoverResources();
	
}
