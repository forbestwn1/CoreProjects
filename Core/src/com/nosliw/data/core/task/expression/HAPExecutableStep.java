package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
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
	
}
