package com.nosliw.core.application.brick.test.complex.script;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.event.HAPInfoEvent;

@HAPEntityWithAttribute
public class HAPTestEvent extends HAPSerializableImp{

	@HAPAttribute
	public static final String EVENTINFO = "eventInfo";
	
	@HAPAttribute
	public static final String EVENTDATA = "eventData";
	
	private HAPInfoEvent m_eventInfo;
	
	private Object m_eventData;
	
	public HAPInfoEvent getEventInfo() {   return this.m_eventInfo;    }
	
	public Object getEventData() {    return this.m_eventData;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.m_eventInfo = new HAPInfoEvent();
		this.m_eventInfo.buildObject(jsonObj.opt(EVENTINFO), HAPSerializationFormat.JSON);

		this.m_eventData = jsonObj.opt(EVENTDATA);
		
		return true;  
	}
	
}
