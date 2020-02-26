package com.nosliw.data.core.cronjob;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPParserCronJobDefinition {

	public HAPParserCronJobDefinition() {
	}

	public HAPDefinitionCronJob parseFile(String fileName){
		HAPDefinitionCronJob out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(input);
			String source = HAPFileUtility.readFile(input);
			out = this.parsePocessCronJob(source, resourceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	public HAPDefinitionCronJob parsePocessCronJob(String content, String id) {
		HAPDefinitionCronJob out = new HAPDefinitionCronJob(id);
		out.buildObject(new JSONObject(content), HAPSerializationFormat.JSON);
		return out;
	}
}
