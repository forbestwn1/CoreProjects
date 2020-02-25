package com.nosliw.data.core.cronjob;

import java.util.Date;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.process.HAPUtilityProcess;

public class HAPDefinitionSchedule extends HAPSerializableImp{

	@HAPAttribute
	public static String START = "start";

	@HAPAttribute
	public static String INTERVAL = "interval";

	@HAPAttribute
	public static String END = "end";

	//when 
	private Date m_start;
	
	//interval between tiguing the task
	private String m_interval;
	
	//
	private HAPDefinitionEnd m_end;

	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		HAPUtilityProcess.parseWithProcessTask(this, jsonObj);
		return true;  
	}

}
