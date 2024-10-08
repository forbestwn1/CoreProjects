package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.domain.entity.task.HAPInfoTask;

/*
 * store 
 */
@HAPEntityWithAttribute
public class HAPElementEvent extends HAPSerializableImp{

	@HAPAttribute
	public static final String UIID = "uiId";
	@HAPAttribute
	public static final String EVENT = "event";
	@HAPAttribute
	public static final String HANDLERNAME = "handlerName";
	@HAPAttribute
	public static final String SELECTION = "selection";
	@HAPAttribute
	public static final String TASKINFO = "taskInfo";
	
	//ui id that this event apply to
	private String m_uiId;

	//event name
	private String m_event;
	//response handler name
	private String m_handlerName;
	//this attribute only appliable to regular element
	//with this attribute set, then the event is based on all child element that meet this selection, rath than the element itself
	private String m_selection;
	//envent handler info
	private HAPInfoTask m_taskInfo;
	
	public HAPElementEvent(String uiId, String eventInfos){
		this.m_uiId = uiId;
		
		HAPSegmentParser events = new HAPSegmentParser(eventInfos, HAPConstantShared.SEPERATOR_PART);
		this.m_event = events.next();
		this.m_handlerName = events.next();
		this.m_selection = events.next();
	}
	
	public void setTaskInfo(HAPInfoTask taskInfo) {    this.m_taskInfo = taskInfo;      }
	
	public String getHandlerName() {    return this.m_handlerName;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(EVENT, this.m_event);
		jsonMap.put(HANDLERNAME, this.m_handlerName);
		jsonMap.put(SELECTION, this.m_selection);
		if(this.m_taskInfo!=null) jsonMap.put(TASKINFO, this.m_taskInfo.toStringValue(HAPSerializationFormat.JSON));
	}
	
}
