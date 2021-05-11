package com.nosliw.data.core.imp.cronjob;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.cronjob.HAPExecutableCronJob;
import com.nosliw.data.core.cronjob.HAPExecutablePollSchedule;
import com.nosliw.data.core.cronjob.HAPManagerScheduleDefinition;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;

public class HAPCronJobInstanceSerializer {

	private HAPManagerScheduleDefinition m_scheduleManDef;
	
	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPCronJobInstanceSerializer(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	public HAPInstanceCronJob parse(String content) {
		JSONObject instanceJsonObj = new JSONObject(content);
		HAPInstanceCronJob out = new HAPInstanceCronJob();
		out.setId(instanceJsonObj.getString(HAPInstanceCronJob.ID));
		out.setCronJob(parseCronJob(instanceJsonObj.getJSONObject(HAPInstanceCronJob.CRONJOB)));
		return out;
	}
	
	public String toString(HAPInstanceCronJob instance) {
		return instance.toStringValue(HAPSerializationFormat.JSON);
	}
	
	private HAPExecutableCronJob parseCronJob(JSONObject jsonObj) {
		HAPExecutableCronJob out = new HAPExecutableCronJob(null, null);
		
		//parse schedule
		JSONObject scheduleJsonObj = jsonObj.optJSONObject(HAPExecutableCronJob.SCHEDULE);
		HAPExecutablePollSchedule schedule = this.m_scheduleManDef.parsePollSchedule(scheduleJsonObj);
		out.setSchedule(schedule);
		
		//task
		JSONObject taskJsonObj = jsonObj.optJSONObject(HAPExecutableCronJob.TASK);
		HAPExecutableWrapperTask<HAPExecutableProcess> taskExe = HAPParserDataAssociation.buildExecutableWrapperTask(taskJsonObj, new HAPExecutableProcess(this.m_activityPluginMan));
		out.setTask(taskExe);
		
		return out;
	}
}
