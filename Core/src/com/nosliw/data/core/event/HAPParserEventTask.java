package com.nosliw.data.core.event;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;

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
		
		return out;
	}
}
