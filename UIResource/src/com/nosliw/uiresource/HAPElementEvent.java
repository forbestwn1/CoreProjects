package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;

/*
 * store 
 */
public class HAPElementEvent extends HAPSerializableImp{
	//ui id that this event apply to
	private String m_uiId;

	//event name
	private String m_event;
	//response function name
	private String m_function;
	//this attribute only appliable to regular element
	//with this attribute set, then the event is based on all child element that meet this selection, rath than the element itself
	private String m_selection;
	
	public HAPElementEvent(String uiId, String eventInfos){
		this.m_uiId = uiId;
		
		HAPSegmentParser events = new HAPSegmentParser(eventInfos, HAPConstant.SEPERATOR_PART);
		this.m_event = events.next();
		this.m_function = events.next();
		this.m_selection = events.next();
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(HAPAttributeConstant.ELEMENTEVENT_UIID, this.m_uiId);
		jsonMap.put(HAPAttributeConstant.ELEMENTEVENT_EVENT, this.m_event);
		jsonMap.put(HAPAttributeConstant.ELEMENTEVENT_FUNCTION, this.m_function);
		jsonMap.put(HAPAttributeConstant.ELEMENTEVENT_SELECTION, this.m_selection);
	}
	
}
