package com.nosliw.uiresource.page;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
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
	public static final String FUNCTION = "function";
	@HAPAttribute
	public static final String SELECTION = "selection";
	
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
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(EVENT, this.m_event);
		jsonMap.put(FUNCTION, this.m_function);
		jsonMap.put(SELECTION, this.m_selection);
	}
	
}
