package com.nosliw.data.core.cronjob;

import com.nosliw.data.core.dataable.HAPExecutableDataable;
import com.nosliw.data.core.runtime.HAPExecutableImpComponent;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

public class HAPExecutableCronJob extends HAPExecutableImpComponent{

	private HAPExecutableWrapperTask m_task; 

	private HAPExecutablePollSchedule m_schedule;
	
	private HAPExecutableWrapperTask<HAPExecutableDataable> m_end;
	
	public HAPExecutableCronJob(HAPDefinitionCronJob cronJobDef, String id) {
		super(cronJobDef);
	}
	
	public void setTask(HAPExecutableWrapperTask task) {     this.m_task = task;      }
	
	public void setEnd(HAPExecutableWrapperTask<HAPExecutableDataable> end) {    this.m_end = end;    }

	public void setSchedule(HAPExecutablePollSchedule schedule) {   this.m_schedule = schedule;    }
	
}
