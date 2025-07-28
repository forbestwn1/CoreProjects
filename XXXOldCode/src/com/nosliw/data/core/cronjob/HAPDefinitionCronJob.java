package com.nosliw.data.core.cronjob;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.component.HAPDefinitionEntityComponentImp;
import com.nosliw.data.core.component.HAPContainerChildReferenceResource;
import com.nosliw.data.core.component.HAPParserEntityComponent;

public class HAPDefinitionCronJob extends HAPDefinitionEntityComponentImp{

	@HAPAttribute
	public static String SCHEDULE = "schedule";
	
	@HAPAttribute
	public static String TASK = "task";
	
	@HAPAttribute
	public static String END = "end";

	private HAPDefinitionSchedule m_schedule;
	
	private HAPDefinitionTask m_task;

	//criteria that end the cron job
	private HAPDefinitionEnd m_end;

	public HAPDefinitionCronJob(String id) {
		super(id);
	}

	public HAPDefinitionSchedule getSchedule() {    return this.m_schedule;    }
	public void setSchedule(HAPDefinitionSchedule schedule) {  this.m_schedule = schedule;  }
	
	public HAPDefinitionTask getTask(){   return this.m_task;     }
	public void setTask(HAPDefinitionTask task) {   this.m_task = task;   }

	public HAPDefinitionEnd getEnd() {    return this.m_end;     }
	

	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		HAPParserEntityComponent.parseComponent(this, jsonObj);
		
		HAPDefinitionSchedule schedule = new HAPDefinitionSchedule();
		schedule.buildObject(jsonObj.getJSONObject(SCHEDULE), HAPSerializationFormat.JSON);
		this.setSchedule(schedule);

		HAPDefinitionTask task = new HAPDefinitionTask();
		task.buildObject(jsonObj.getJSONObject(TASK), HAPSerializationFormat.JSON);
		this.setTask(task);
		
		this.m_end = new HAPDefinitionEnd();
		this.m_end.buildEmbededAttachmentReferenceByJson(jsonObj.optJSONObject(END), null);
		
		return true;  
	}

	@Override
	public HAPDefinitionEntityComponent cloneComponent() {
		// TODO Auto-generated method stub
		return null;
	}
}
