package com.nosliw.ui.entity.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPSegmentParser;

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
	
	//ui id that this event apply to
	private String m_uiId;

	//event name
	private String m_event;
	//response handler name
	private String m_handlerName;
	//this attribute only appliable to regular element
	//with this attribute set, then the event is based on all child element that meet this selection, rath than the element itself
	private String m_selection;
	
	public HAPElementEvent(String uiId, String eventInfos){
		this.m_uiId = uiId;
		
		HAPSegmentParser events = new HAPSegmentParser(eventInfos, HAPConstantShared.SEPERATOR_PART);
		this.m_event = events.next();
		this.m_handlerName = events.next();
		this.m_selection = events.next();
	}
	
	public String getHandlerName() {    return this.m_handlerName;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(EVENT, this.m_event);
		jsonMap.put(HANDLERNAME, this.m_handlerName);
		jsonMap.put(SELECTION, this.m_selection);
	}
	
}
