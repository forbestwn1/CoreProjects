package com.nosliw.common.boolop;

import java.util.List;

public class HAPBooleanOperationOr extends HAPBooleanTaskImp{

	private List<HAPBooleanTask> m_tasks;
	
	@Override
	public boolean execute(Object data) {
		boolean out = false;
		for(HAPBooleanTask task : this.m_tasks){
			out = out || task.equals(data);
		}
		return out;
	}
}
