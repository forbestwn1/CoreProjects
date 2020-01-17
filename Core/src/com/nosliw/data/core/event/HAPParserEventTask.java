package com.nosliw.data.core.event;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPComponentUtility;

public class HAPParserEventTask {
	private static HAPParserEventTask m_instance;
	private HAPParserEventTask() {}
	
	public static HAPParserEventTask getInstance() {
		if(m_instance==null) {
			m_instance = new HAPParserEventTask();
		}
		return m_instance;
	}
	
	public HAPDefinitionEventTask parseFile(String fileName){
		HAPDefinitionEventTask out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(input);
			String source = HAPFileUtility.readFile(input);
			out = this.parseContent(source, resourceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private HAPDefinitionEventTask parseContent(String content, String id) {
		JSONObject jsonObj = new JSONObject(content);
		HAPDefinitionEventTask out = new HAPDefinitionEventTask(id);
		
		HAPComponentUtility.parseComponent(out, jsonObj);
		out.setEventInfo(this.parseEventInfo(jsonObj.optJSONObject(HAPDefinitionEventTask.EVENTINFO)));
		out.setEventSourceInfo(this.parseSource(jsonObj.optJSONObject(HAPDefinitionEventTask.SOURCE)));
		out.setEventHandle((this.parseEventHandler(jsonObj.optJSONObject(HAPDefinitionEventTask.HANDLER))));
		
		return out;
	}
	
	private HAPDefinitionEventSource parseSource(JSONObject jsonObj) {
		HAPDefinitionEventSource out = new HAPDefinitionEventSource();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	private HAPDefinitionEvent parseEventInfo(JSONObject jsonObj) {
		HAPDefinitionEvent out = new HAPDefinitionEvent();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	private HAPDefinitionEventHandle parseEventHandler(JSONObject jsonObj) {
		HAPDefinitionEventHandle out = new HAPDefinitionEventHandle();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
}
