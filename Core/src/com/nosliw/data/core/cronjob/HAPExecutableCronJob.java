package com.nosliw.data.core.cronjob;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataable.HAPExecutableDataable;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.runtime.HAPExecutableImpComponent;

public class HAPExecutableCronJob extends HAPExecutableImpComponent{

	@HAPAttribute
	public static String TASK = "task";

	@HAPAttribute
	public static String SCHEDULE = "schedule";

	@HAPAttribute
	public static String END = "end";

	private HAPExecutableWrapperTask<HAPExecutableProcess> m_task; 

	private HAPExecutablePollSchedule m_schedule;
	
	private HAPExecutableWrapperTask<HAPExecutableDataable> m_end;
	
	public HAPExecutableCronJob(HAPDefinitionCronJob cronJobDef, String id) {
		super(cronJobDef);
	}
	
	public void setTask(HAPExecutableWrapperTask<HAPExecutableProcess> task) {     this.m_task = task;      }
	
	public void setEnd(HAPExecutableWrapperTask<HAPExecutableDataable> end) {    this.m_end = end;    }

	public void setSchedule(HAPExecutablePollSchedule schedule) {   this.m_schedule = schedule;    }
	
	public HAPExecutablePollSchedule getSchedule() {    return this.m_schedule;    }


	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCHEDULE, this.m_schedule.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TASK, this.m_task.toStringValue(HAPSerializationFormat.JSON));
	}


}
