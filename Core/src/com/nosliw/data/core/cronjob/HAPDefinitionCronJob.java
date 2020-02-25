package com.nosliw.data.core.cronjob;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPDefinitionCronJob extends HAPComponentImp{

	@HAPAttribute
	public static String SCHEDULE = "schedule";
	
	private HAPDefinitionSchedule m_schedule;
	
	private HAPDefinitionWrapperTask<String> m_task;

	public HAPDefinitionCronJob(String id, HAPManagerActivityPlugin activityPluginMan) {
		super(id, activityPluginMan);
	}

	public HAPDefinitionSchedule getSchedule() {    return this.m_schedule;    }
	public void setSchedule(HAPDefinitionSchedule schedule) {  this.m_schedule = schedule;  }
	
	public HAPDefinitionWrapperTask<String> getTask(){   return this.m_task;     }
	public void setTask(HAPDefinitionWrapperTask<String> task) {   this.m_task = task;   }
	
	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
