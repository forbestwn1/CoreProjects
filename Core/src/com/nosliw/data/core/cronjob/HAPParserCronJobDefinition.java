package com.nosliw.data.core.cronjob;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;

public class HAPParserCronJobDefinition {

	private static HAPParserCronJobDefinition m_instance;
	
	public static HAPParserCronJobDefinition getInstance() {
		if(m_instance==null)   m_instance = new HAPParserCronJobDefinition();
		return m_instance;
	}
	
	private HAPParserCronJobDefinition() {}

	public HAPDefinitionCronJob parseFile(String fileName){
		HAPDefinitionCronJob out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPUtilityFile.getFileName(input);
			String source = HAPUtilityFile.readFile(input);
			out = this.parseCronJob(source, resourceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	public HAPDefinitionCronJob parseCronJob(String content, String id) {
		HAPDefinitionCronJob out = new HAPDefinitionCronJob(id);
		out.buildObject(new JSONObject(content), HAPSerializationFormat.JSON);
		return out;
	}

	public HAPDefinitionCronJob parseCronJob(JSONObject jsonObj, String id) {
		HAPDefinitionCronJob out = new HAPDefinitionCronJob(id);
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
}
